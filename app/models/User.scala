package models

import java.util.UUID
import scala.slick.driver.PostgresDriver.simple._
import lib.Global

/**
 * Created by Adam on 5/18/14.
 */


case class UserProfile(var id: UUID = Global.UUIDZero, username: String = "",
                       email: String = "", first_name: String = "", last_name: String = "")

case class UserAuth(var id: UUID = Global.UUIDZero, username: String = "",
                    email: String = "", password: String = "", salt: String = "")

case class User(var id: UUID = Global.UUIDZero, username: String = "", email: String = "", password: String = "",
                salt: String = "", first_name: String = "", last_name: String = "") {
  def profile = UserProfile(id, username, email, first_name, last_name)
  def auth = UserAuth(id, username, email, password, salt)
}

class Users(tag: Tag) extends Table[User](tag, Some("public"), "user") {
  def id = column[UUID]("id", O.PrimaryKey)
  def username = column[String]("username")
  def email = column[String]("email")
  def password = column[String]("password")
  def salt = column[String]("salt")
  def first_name = column[String]("first_name")
  def last_name = column[String]("last_name")

  def * = (id, username, email, password, salt, first_name, last_name) <> (User.tupled, User.unapply)
}

