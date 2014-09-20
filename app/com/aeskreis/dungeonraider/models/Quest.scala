package com.aeskreis.dungeonraider.models

import java.util.UUID
import com.aeskreis.dungeonraider.lib.Global
import play.api.libs.json._
import scala.slick.driver.PostgresDriver.simple._

/**
 * Created by Adam on 6/8/14.
 */
case class Quest(var id: UUID = Global.UUIDZero, name: String = "", body: String = "",
                  follow_up_id: UUID = Global.UUIDZero, start_npc_id: UUID = Global.UUIDZero,
                  end_npc_id:UUID = Global.UUIDZero, goals: String = "",
                  prereqs: String = "", repeat_frequency: Int = 0)

class Quests(tag: Tag) extends Table[Quest](tag, Some("public"), "quest") {
  def id = column[UUID]("id", O.PrimaryKey)
  def name = column[String]("name")
  def body = column[String]("body")
  def follow_up_id = column[UUID]("follow_up_id")
  def start_npc_id = column[UUID]("start_npc_id")
  def end_npc_id = column[UUID]("end_npc_id")
  def goals = column[String]("goals")
  def prereqs = column[String]("prereqs")
  def repeat_frequency = column[Int]("repeat_frequency")

  def * = (id, name, body, follow_up_id, start_npc_id, end_npc_id,
    goals, prereqs, repeat_frequency) <> (Quest.tupled, Quest.unapply)
}
