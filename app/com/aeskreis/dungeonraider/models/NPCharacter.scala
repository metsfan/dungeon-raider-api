package com.aeskreis.dungeonraider.models

/**
 * Created by Adam on 2/8/14.
 */

import java.util.UUID
import scala.slick.driver.PostgresDriver.simple._

case class NPCharacter(var id: UUID = null, name: String = "", health: Int = 0, race: Int = 0,
                       level: Int = 0, class_id: Int = 0, model: String = "", hostility: Int = 0,
                       faction: Int = 0, agro_radius: Double = 0.0) {
  var spells: List[Spell] = List[Spell]()
}

case class NPCharacterSpell(var id: UUID = null, spell_id: UUID = null, char_id: UUID = null)

class NPCharacters(tag: Tag) extends Table[NPCharacter](tag, Some("public"), "npcharacter") {
  def id = column[UUID]("id", O.PrimaryKey)
  def name = column[String]("name")
  def health = column[Int]("health")
  def race = column[Int]("race")
  def level = column[Int]("level")
  def class_id = column[Int]("class_id")
  def model = column[String]("model")
  def hostility = column[Int]("hostility")
  def faction = column[Int]("faction")
  def agro_radius = column[Double]("agro_radius")

  def * = (id, name, health, race, level, class_id, model, hostility,
    faction, agro_radius) <> (NPCharacter.tupled, NPCharacter.unapply)
}

class NPCharacterSpells(tag: Tag) extends Table[NPCharacterSpell](tag, Some("public"), "npcharacter_spell") {
  def id = column[UUID]("id", O.PrimaryKey)
  def spell_id = column[UUID]("spell_id")
  def char_id = column[UUID]("spell_id")

  def * = (id, spell_id, char_id) <> (NPCharacterSpell.tupled, NPCharacterSpell.unapply)
}
