package com.aeskreis.dungeonraider.lib.json

import play.api.libs.json._
import play.api.libs.json.Writes._
import com.github.t3hnar.bcrypt._
import com.aeskreis.dungeonraider.models._
import play.api.libs.json.Json._
import anorm.RowParser
import com.aeskreis.dungeonraider.models.User

/**
 * Created by Adam on 5/25/14.
 */
class UserParser extends JsonObjectParser[User] {
  implicit object UuidWrites extends Writes[java.util.UUID] {
    def writes(u: java.util.UUID) = JsString(u.toString())
  }

  implicit val userWrites = writes[User]
  implicit val userReads = reads[User]

  implicit val townWrites = writes[Town]

  val profileParser = new UserProfileParser

  def registerFormParser(data: Map[String, Seq[String]]) = {
    val username = data.get("username").get.head
    val password = data.get("password").get.head
    val email = data.get("email").get.head
    val first_name = data.get("first_name").get.head
    val last_name = data.get("last_name").get.head

    val salt = generateSalt
    val hashedPassword = password.bcrypt(salt)

    User(null, username, email, hashedPassword, salt, first_name, last_name)
  }

  def loginFormParser(data: Map[String, Seq[String]]) = {
    val username = data.get("username").getOrElse(Seq[String]()).headOption.getOrElse("")
    val password = data.get("password").getOrElse(Seq[String]()).headOption.getOrElse("")
    val email = data.get("email").getOrElse(Seq[String]()).headOption.getOrElse("")

    UserAuth(null, username, email, password, "")
  }

  override def toObject(obj: Option[User], data: JsValue): JsResult[User] = {
    _toObject(obj.getOrElse(User()), data, userWrites, userReads)
  }

  override def toJsonArray(objs: List[User]): JsValue = {
    _toJsonArray(objs, "users", userWrites)
  }

  override def toJsonObject(obj: User): JsValue = {
    _toJsonObject(obj, "user", userWrites)
  }

  def toJsonArrayProfile(objs: List[UserProfile]): JsValue = {
    profileParser.toJsonArray(objs)
  }

  def toJsonObject(obj: UserProfile): JsValue = {
    profileParser.toJsonObject(obj)
  }
  class UserProfileParser extends JsonObjectParser[UserProfile] {
    val userProfileWrites = writes[UserProfile]
    val userProfileReads = reads[UserProfile]

    override def toObject(obj: Option[UserProfile], data: JsValue): JsResult[UserProfile] = {
      _toObject(obj.getOrElse(UserProfile()), data, userProfileWrites, userProfileReads)
    }

    override def toJsonArray(objs: List[UserProfile]): JsValue = {
      _toJsonArray(objs, "users", userProfileWrites)
    }

    override def toJsonObject(obj: UserProfile): JsValue = {
      _toJsonObject(obj, "user", userProfileWrites)
    }
  }
}
