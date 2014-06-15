package models

import java.util.UUID
import scala.slick.driver.PostgresDriver.simple._
import lib.Global

case class PCharacter(var id: UUID = Global.UUIDZero, user_id: UUID = Global.UUIDZero, name: String = "",
                      class_id: Int = 0, model: String = "", health: Int = 0, race: Int = 0, level: Int = 0,
                      experience: Int = 0) {
  var spells: List[Spell] = List[Spell]()
}

class PCharacters(tag: Tag) extends Table[PCharacter](tag, Some("public"), "pcharacter") {
  def id = column[UUID]("id")
  def user_id = column[UUID]("user_id")
  def name = column[String]("name")
  def class_id = column[Int]("class_id")
  def model = column[String]("model")
  def health = column[Int]("health")
  def race = column[Int]("race")
  def level = column[Int]("level")
  def experience = column[Int]("experience")

  def * = (id, user_id, name, class_id, model, health, race,
    level, experience) <> (PCharacter.tupled, PCharacter.unapply)
}
