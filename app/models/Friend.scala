package models

import scala.slick.driver.PostgresDriver.simple._
import java.util.UUID
import lib.Global

/**
 * Created by Adam on 8/30/14.
 */

object FriendStatus extends Enumeration {
  val Pending = Value(0)
  val Accepted = Value(1)
}

case class Friend(var id: UUID = Global.UUIDZero, user1: UUID = Global.UUIDZero,
                  user2: UUID = Global.UUIDZero, var status: Int = 0)

class Friends(tag: Tag) extends Table[Friend](tag, Some("public"), "friend") {
  def id = column[UUID]("id", O.PrimaryKey)
  def user1 = column[UUID]("user1")
  def user2 = column[UUID]("user2")
  def status = column[Int]("status")

  def * = (id, user1, user2, status) <> (Friend.tupled, Friend.unapply)
}
