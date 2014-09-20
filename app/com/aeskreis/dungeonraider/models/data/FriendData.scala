package com.aeskreis.dungeonraider.models.data

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
  val users = TableQuery[Users]

  def allForUser(id: UUID, status: Int = FriendStatus.Accepted.id): List[User] = {
    DB.withSession { implicit session =>
      (for {
        f <- friends if f.user1 === id && f.status === status
        u <- users if f.user2 === u.id
      } yield u).list
    }
  }

  def save(friend: Friend): Option[Friend] = {
    DB.withSession { implicit session =>
      if (friend.id == null) {
        friend.id = UUID.randomUUID()
        friends.insert(friend)
      } else {
        friends.filter(_.id === friend.id).update(friend)
      }

      Some(friend)
    }
  }

  def get(user1: UUID, user2: UUID): Option[Friend] = {
    DB.withSession { implicit session =>
      val query = friends.filter(f => {
        f.user1 === user1 && f.user2 === user2
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

  def delete(id: UUID): Int = {
    DB.withSession { implicit session =>
      friends.filter(_.id === id).delete
    }
  }
}
