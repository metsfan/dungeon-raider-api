package models.data

import models.{Users, UserProfile, UserAuth, User}
import java.util.UUID
import models.query.UserQuery
import com.github.t3hnar.bcrypt._
import scala.slick.driver.PostgresDriver.simple._
import play.api.Play.current
import play.api.db.slick.DB

/**
 * Created by Adam on 5/25/14.
 */
class UserData extends BaseData {
  val users = TableQuery[Users]

  def getById(id: UUID): Option[User] = {
    DB.withSession { implicit session =>
      users.filter(_.id === id).firstOption
    }
  }

  def getByUsername(username: String): Option[User] = {
    DB.withSession { implicit session =>
      users.filter(_.username.toLowerCase === username.toLowerCase).firstOption
    }
  }

  def getByEmail(email: String): Option[User] = {
    DB.withSession { implicit session =>
      users.filter(_.email === email).firstOption
    }
  }

  def save(user: User): User = {
    DB.withSession { implicit session =>
      val isInsert = user.id != null
      if (isInsert) {
        user.id = UUID.randomUUID
      }

      /*val fields: Seq[(Any, ParameterValue[_])] = Seq(
        "id" -> user.id,
        "username" -> user.profile.username,
        "email" -> user.profile.email,
        "password" -> user.auth.password,
        "salt" -> user.auth.salt,
        "first_name" -> user.profile.first_name,
        "last_name" -> user.profile.last_name
      )

      if (isInsert) {
        SQL(UserQuery.insertUser).on(fields: _*).execute()
      } else {
        SQL(UserQuery.updateUser).on(fields: _*).executeUpdate()
      }*/

      user
    }
  }

  def login(user: UserAuth): Option[User] = {
    DB.withSession { implicit session =>
      val dbUser = if (user.username.length > 0) {
        this.getByUsername(user.username)
      } else if (user.email.length == 0) {
        this.getByEmail(user.email)
      } else {
        None
      }

      if (dbUser.isDefined) {
        val testPw = user.password.bcrypt(dbUser.get.auth.salt)
        if (testPw == dbUser.get.auth.password) {
          // Hashed passwords match, return database user object
          dbUser
        } else {
          throw new RuntimeException("Incorrect Password")
        }
      } else {
        throw new RuntimeException("Username or email does not exist")
      }
    }
  }
}
