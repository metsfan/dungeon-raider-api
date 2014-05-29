package models.data

import models.parsers.UserParser
import models.{UserProfile, UserAuth, User}
import play.api.db.DB
import anorm._
import java.util.UUID
import models.query.UserQuery
import com.github.t3hnar.bcrypt._
import play.api.Play.current

/**
 * Created by Adam on 5/25/14.
 */
class UserData extends BaseData with UserParser {

  def getById(id: String): Option[User] = {
    DB.withConnection {
      implicit conn => {
        SQL(UserQuery.selectUser).on("id" -> id).as(userRowParser *).headOption
      }
    }
  }

  def getByUsername(username: String): Option[User] = {
    DB.withConnection {
      implicit conn => {
        SQL(UserQuery.selectUserByUsername).on("username" -> username).as(userRowParser *).headOption
      }
    }
  }

  def getByEmail(email: String): Option[User] = {
    DB.withConnection {
      implicit conn => {
        SQL(UserQuery.selectUserByEmail).on("email" -> email).as(userRowParser *).headOption
      }
    }
  }

  def getUserAuthById(id: String): Option[UserAuth] = {
    DB.withConnection {
      implicit conn => {
        SQL(UserQuery.selectUser).on("id" -> id).as(userAuthRowParser *).headOption
      }
    }
  }

  def getUserAuthByUsername(username: String): Option[UserAuth] = {
    DB.withConnection {
      implicit conn => {
        SQL(UserQuery.selectUserByUsername).on("username" -> username).as(userAuthRowParser *).headOption
      }
    }
  }

  def getUserAuthByEmail(email: String): Option[UserAuth] = {
    DB.withConnection {
      implicit conn => {
        SQL(UserQuery.selectUserByEmail).on("email" -> email).as(userAuthRowParser *).headOption
      }
    }
  }

  def save(user: User): User = {
    DB.withConnection {
      implicit conn => {
        val isInsert = user.id.length == 0
        if (isInsert) {
          user.id = UUID.randomUUID.toString
        }

        val fields: Seq[(Any, ParameterValue[_])] = Seq(
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
        }

        user
      }
    }
  }

  def login(user: UserAuth): Option[User] = {
    DB.withConnection {
      implicit conn => {
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
}
