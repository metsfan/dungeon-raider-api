package controllers

import play.api.mvc._
import java.util.UUID
import models.data.{UserData, FriendData}
import lib.json.FriendParser
import models.{Friend, FriendStatus}
import play.api.libs.json.Json

/**
 * Created by Adam on 8/30/14.
 */
object FriendController extends Controller {
  val friendData = new FriendData
  val userData = new UserData

  val friendParser = new FriendParser

  def list(user_id: UUID) = Action {
    Ok(friendParser.toJsonArray(friendData.allForUser(user_id))).as("application/json")
  }

  def pendingFriendRequests(user_id: UUID) = Action {
    Ok(friendParser.toJsonArray(friendData.allForUser(user_id, FriendStatus.Pending.id)))
  }

  def request(user_id: UUID) = Action { implicit request =>
    request.body match {
      case AnyContentAsFormUrlEncoded(data) => {
        val requestedName = data.get("username")
        if (requestedName.isDefined && requestedName.size > 0) {
          val requestedUser = userData.getByUsername(requestedName.get(0))
          if (requestedUser.isDefined) {
            if (!friendData.get(user_id, requestedUser.get.id).isDefined) {
              val friend = Friend(null, user_id, requestedUser.get.id, FriendStatus.Pending.id)
              Ok(friendParser.toJsonObject(friendData.save(friend).get))
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
            val friend = friendData.get(user_id, requesterUser.get.id)
            if (friend.isDefined) {
              action match {
                case "accept" => {
                  friend.get.status = FriendStatus.Accepted.id
                  friendData.save(friend.get)
                }
                case "reject" => {
                  friendData.delete(friend.get.user1, friend.get.user2)
                }
              }

              Ok(friendParser.toJsonObject(friendData.save(friend.get).get))
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
