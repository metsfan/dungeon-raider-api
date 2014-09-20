package com.aeskreis.dungeonraider.controllers

import play.api.mvc.Action
import models.data.{TownData, UserData}
import play.api.libs.json.Json._
import models.{UserProfile, User}
import scala.slick.lifted.TableQuery
import java.util.UUID
import com.aeskreis.dungeonraider.lib.json.UserParser

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/30/13
 * Time: 9:07 AM
 * To change this template use File | Settings | File Templates.
 */
object UserController extends BaseController {
  val userData = new UserData
  val townData = new TownData

  val parser = new UserParser

  def register() = Action {
    implicit request => {
      val formData = request.body.asFormUrlEncoded
      val result = if (formData.isDefined) {
        val user = userData.save(parser.registerFormParser(formData.get))
        parser.toJsonObject(user.profile)
      } else {
        toJson(Map(
          "success" -> false
        ))
      }

      Ok(result)
    }
  }

  def login() = Action {
    implicit request => {
      val formData = request.body.asFormUrlEncoded
      val result = if(formData.isDefined) {
        val user = userData.login(parser.loginFormParser(formData.get))
        if (user.isDefined) {
          parser.toJsonObject(user.get.profile)
        } else {
          toJson(Map(
            "success" -> false
          ))
        }
      } else {
        toJson(Map(
          "success" -> false
        ))
      }

      Ok(result)
    }
  }

  def get(id: UUID) = Action {
    val user = userData.getById(id)

    val result = if (user.isDefined) {
      parser.toJsonObject(user.get.profile)
    } else {
      toJson(Map(
        "success" -> false
      ))
    }

    Ok(result)
  }
}
