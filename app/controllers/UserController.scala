package controllers

import play.api.mvc.Action
import models.parsers.UserParser
import models.data.{TownData, UserData}
import play.api.libs.json.Json._
import models.{UserProfile, User}
import scala.slick.lifted.TableQuery
import java.util.UUID

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/30/13
 * Time: 9:07 AM
 * To change this template use File | Settings | File Templates.
 */
object UserController extends BaseController with UserParser {
  val userData = new UserData
  val townData = new TownData

  def register() = Action {
    implicit request => {
      val formData = request.body.asFormUrlEncoded
      val result = if (formData.isDefined) {
        val user = userData.save(registerFormParser(formData.get))
        jsonify(user.profile)
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
        val user = userData.login(loginFormParser(formData.get))
        if (user.isDefined) {
          jsonify(user.get.profile)
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
      jsonify(user.get.profile)
    } else {
      toJson(Map(
        "success" -> false
      ))
    }

    Ok(result)
  }
}
