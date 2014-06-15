package models

import java.util.UUID
import scala.slick.driver.PostgresDriver.simple._

case class Town(id: UUID = null, name: String = "", map: String = "", zone: Int = 0) {
  var is_default: Boolean = false
  var leader: Boolean = false
}
case class _UserTown(id: UUID = null, user_id: UUID = null, town_id: UUID = null,
                     rank: Int = 0, is_default: Boolean = false, leader: Boolean = false)

case class UserTown(id: UUID, name: String, map: String, zone: Int, rank: Int, is_default: Boolean, leader: Boolean)

class Towns(tag: Tag) extends Table[Town](tag, Some("public"), "town") {
  def id = column[UUID]("id", O.PrimaryKey)
  def name = column[String]("name")
  def map = column[String]("map")
  def zone = column[Int]("zone")

  def * = (id, name, map, zone) <> (Town.tupled, Town.unapply)
}

class UserTowns(tag: Tag) extends Table[_UserTown](tag, Some("public"), "user_town") {
  def id = column[UUID]("id", O.PrimaryKey)
  def user_id = column[UUID]("user_id")
  def town_id = column[UUID]("town_id")
  def rank = column[Int]("rank")
  def is_default = column[Boolean]("is_default")
  def leader = column[Boolean]("leader")

  def * = (id, user_id, town_id, rank, is_default, leader) <> (_UserTown.tupled, _UserTown.unapply)
}
