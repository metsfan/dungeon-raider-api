package models

import java.util.UUID
import scala.slick.driver.PostgresDriver.simple._

case class PCharacter(var id: UUID, user_id: UUID, name: String, class_id: Int, model: String, health: Int,
                      race: Int, level: Int, experience: Int)

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
