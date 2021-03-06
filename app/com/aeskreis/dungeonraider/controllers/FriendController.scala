package com.aeskreis.dungeonraider.controllers

import java.io.{ByteArrayOutputStream, ByteArrayInputStream}

import com.aeskreis.dungeonraider.lib.{ServerCommands, Global}
import org.apache.commons.io.EndianUtils
import play.api.mvc._
import java.util.UUID
import com.aeskreis.dungeonraider.models.data.{UserData, FriendData}
import com.aeskreis.dungeonraider.lib.json.{UserParser, FriendParser}
import com.aeskreis.dungeonraider.models.{Friend, FriendStatus}
import play.api.libs.json.Json
import com.aeskreis.dungeonraider.lib.StreamAddins._

/**
 * Created by Adam on 8/30/14.
 */
object FriendController extends Controller {
  val friendData = new FriendData
  val userData = new UserData

  val friendParser = new FriendParser
  val userParser = new UserParser

  def list(user_id: UUID) = Action {
    Ok(userParser.toJsonArrayProfile(
      friendData.allForUser(user_id, FriendStatus.Accepted.id).map(_.profile))
    ).as("application/json")
  }

  def pendingFriendRequests(user_id: UUID) = Action {
    Ok(userParser.toJsonArrayProfile(
      friendData.allForUser(user_id, FriendStatus.Pending.id).map(_.profile))
    ).as("application/json")
  }

  def request(user_id: UUID) = Action { implicit request =>
    request.body match {
      case AnyContentAsFormUrlEncoded(data) => {
        val requestedName = data.get("username")
        if (requestedName.isDefined && requestedName.size > 0) {
          val requestedUser = userData.getByUsername(requestedName.get(0))
          if (requestedUser.isDefined) {
            if (!friendData.get(user_id, requestedUser.get.id).isDefined) {
              val friend1 = Friend(null, user_id, requestedUser.get.id, FriendStatus.Pending.id)
              friendParser.toJsonObject(friendData.save(friend1).get)

              val friend2 = Friend(null, requestedUser.get.id, user_id, FriendStatus.Pending.id)
              friendParser.toJsonObject(friendData.save(friend2).get)

              // Publish to chat server
              val requester = userData.getById(user_id)
              val message = new ByteArrayOutputStream()
              EndianUtils.writeSwappedInteger(message, ServerCommands.FriendRequest.id)
              message.writeUUID(user_id)
              message.writeUUID(requestedUser.get.id)
              message.writeCString(userParser.toJsonObject(requester.get).toString())

              Global.consumerChannel.basicPublish("", "game_chat_server", null, message.toByteArray)

              Ok(Json.obj("success" -> true))
            } else {
              BadRequest
            }
          } else {
            BadRequest
          }
        } else {
          BadRequest
        }
      }
      case _ => BadRequest
    }
  }

  def response(user_id: UUID) = Action { implicit request =>
    request.body match {
      case AnyContentAsFormUrlEncoded(data) => {
        val requesterId = data.get("requester_id")
        if (requesterId.isDefined && requesterId.size > 0) {
          val requesterUser = userData.getById(UUID.fromString(requesterId.get(0)))
          if (requesterUser.isDefined) {
            val action = data.get("action").get(0)

            val friend1 = friendData.get(user_id, requesterUser.get.id)
            val friend2 = friendData.get(requesterUser.get.id, user_id)

            if (friend1.isDefined && friend2.isDefined) {
              action match {
                case "accept" => {
                  friend1.get.status = FriendStatus.Accepted.id
                  friendData.save(friend1.get)

                  friend2.get.status = FriendStatus.Accepted.id
                  friendData.save(friend2.get)

                  // Publish to chat server
                  val requested = userData.getById(user_id)
                  val message = new ByteArrayOutputStream()
                  EndianUtils.writeSwappedInteger(message, ServerCommands.FriendResponse.id)

                  message.writeUUID(requesterUser.get.id)
                  message.writeUUID(user_id)
                  message.writeCString(userParser.toJsonObject(requested.get).toString())

                  Global.consumerChannel.basicPublish("", "game_chat_server", null, message.toByteArray)
                }
                case "reject" => {
                  friendData.delete(friend1.get.id)
                  friendData.delete(friend2.get.id)
                }
              }

              Ok(Json.obj("success" -> true))
            } else {
              BadRequest
            }
          } else {
            BadRequest
          }
        } else {
          BadRequest
        }
      }
      case _ => BadRequest
    }
  }

  def delete(user_id: UUID) = Action { implicit request =>
    request.body match {
      case AnyContentAsFormUrlEncoded(data) => {
        val friendId = data.get("friend_id").get(0)
        Ok(Json.obj(
          "success" -> friendData.delete(user_id, UUID.fromString(friendId)))
        )
      }

      case _ => BadRequest
    }
  }
}
