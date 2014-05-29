package models.parsers

import play.api.libs.json._
import play.api.libs.json.util._
import play.api.libs.json.Writes._
import java.util.UUID
import com.github.t3hnar.bcrypt._
import models._
import play.api.libs.json.Json._
import anorm.SqlParser._
import anorm.{RowParser, ~}
import anorm.~
import models.User

/**
 * Created by Adam on 5/25/14.
 */
trait UserParser {

  implicit val userProfileWrites = writes[UserProfile]

  def userRowParser(): RowParser[User] = {
    get[String]("id") ~
    get[String]("username") ~
    get[String]("email") ~
    get[String]("password") ~
    get[String]("salt") ~
    get[String]("first_name") ~
    get[String]("last_name") map {
      case id ~ username ~ email ~ password ~ salt ~ firstName ~ lastName =>
        User(id, UserAuth(id, username, email, password, salt),
          UserProfile(id, username, email, firstName, lastName))
    }
  }

  def userAuthRowParser(): RowParser[UserAuth] = {
    get[String]("id") ~
      get[String]("username") ~
      get[String]("email") ~
      get[String]("password") ~
      get[String]("salt") map {
      case id ~ username ~ email ~ password ~ salt =>
        UserAuth(id, username, email, password, salt)
    }
  }

  def registerFormParser(data: Map[String, Seq[String]]) = {
    val username = data.get("username").get.head
    val password = data.get("password").get.head
    val email = data.get("email").get.head
    val first_name = data.get("first_name").get.head
    val last_name = data.get("last_name").get.head

    val salt = generateSalt
    val hashedPassword = password.bcrypt(salt)

    User("", UserAuth("", username, email, hashedPassword, salt),
      UserProfile("", username, email, first_name, last_name))
  }

  def loginFormParser(data: Map[String, Seq[String]]) = {
    val username = data.get("username").getOrElse(Seq[String]()).headOption.getOrElse("")
    val password = data.get("password").getOrElse(Seq[String]()).headOption.getOrElse("")
    val email = data.get("email").getOrElse(Seq[String]()).headOption.getOrElse("")

    UserAuth("", username, email, password, "")
  }

  def jsonify(user: List[UserProfile]): JsValue = {
    toJson(user)
  }

  def jsonify(user: UserProfile): JsValue = {
    toJson(user)
  }
}
