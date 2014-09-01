package models.data

/**
 * Created by Adam on 8/30/14.
 */

import play.api.Play.current
import models._
import scala.slick.driver.PostgresDriver.simple._
import play.api.db.slick.DB
import java.util.UUID
import play.api.Logger

class FriendData extends BaseData {
  val friends = TableQuery[Friends]

  def allForUser(id: UUID, status: Int = FriendStatus.Accepted.id): List[Friend] = {
    DB.withSession { implicit session =>
      friends.filter(f => (f.user1 === id || f.user2 === id) && f.status === status).list
    }
  }

  def save(friend: Friend): Option[Friend] = {
    DB.withSession { implicit session =>
      if (friend.id == null) {
        friend.id = UUID.randomUUID()
        friends.insert(friend)
      } else {
        friends.update(friend)
      }

      Some(friend)
    }
  }

  def get(user1: UUID, user2: UUID): Option[Friend] = {
    DB.withSession { implicit session =>
      val query = friends.filter(f => {
        ((f.user1 === user1 && f.user2 === user2) ||
            (f.user1 === user2 && f.user2 === user1))
      })

      query.list.headOption
    }
  }

  def delete(user1: UUID, user2: UUID): Int = {
    DB.withSession { implicit session =>
      friends.filter(f => {
        ((f.user1 === user1 && f.user2 === user2) ||
          (f.user1 === user2 && f.user2 === user1))
      }).delete
    }
  }
}
