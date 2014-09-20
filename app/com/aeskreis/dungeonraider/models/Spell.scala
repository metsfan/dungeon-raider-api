package com.aeskreis.dungeonraider.models

import scala.slick.driver.PostgresDriver.simple._
import java.util.UUID
import com.aeskreis.dungeonraider.lib.Global

/**
 * Created by Adam on 2/8/14.
 */

case class Spell(var id: UUID = Global.UUIDZero, name: String = "", cast_time: Int = 0, cooldown: Int = 0,
                 spell_type: Int = 0, cast_type: Int = 0, spell_radius: Double = 0.0, spell_range: Double = 0,
                 shape: Int = 0, self_cast: Boolean = false, class_id: Int = 0, icon_url: Option[String] = None) {
  var effects: List[SpellEffect] = List[SpellEffect]()
  var triggers: List[SpellTrigger] = List[SpellTrigger]()
  var charClass: Option[CharClass] = None
  var classSpell: Option[ClassSpell] = None
}

case class SpellEffect(var id: UUID = Global.UUIDZero, var spell_id: UUID = Global.UUIDZero, effect_type: Int = 0,
                        damage_source: Int = 0, buff_source: Int = 0, percent_source_min: Int = 0,
                        percent_source_max: Int = 0, flat_amount_min: Int = 0, flat_amount_max: Int = 0,
                        dot_tick: Int = 0, dot_duration: Int = 0, buff_duration: Int = 0, mechanic: Int = 0,
                        school: Int = 0, script_name: Option[String] = None, script_arguments: Option[String] = None,
                        delta: Int = 0, max_stacks: Int = 0)

case class SpellTrigger(var id: UUID = Global.UUIDZero, var spell_id: UUID = Global.UUIDZero, trigger_spell_id: UUID = Global.UUIDZero,
                        chance: Double = 0.0, trigger_type: Int = 0)

case class ClassSpell(var id: UUID = Global.UUIDZero, spell_id: UUID = Global.UUIDZero, class_id: Int = 0,
                       slot: Option[String] = None)

class Spells(tag: Tag) extends Table[Spell](tag, Some("public"), "spell") {
  def id = column[UUID]("id", O.PrimaryKey)
  def name = column[String]("name")
  def cast_time = column[Int]("cast_time")
  def cooldown = column[Int]("cooldown")
  def spell_type = column[Int]("spell_type")
  def cast_type = column[Int]("cast_type")
  def spell_radius = column[Double]("spell_radius")
  def spell_range = column[Double]("spell_range")
  def shape = column[Int]("shape")
  def self_cast = column[Boolean]("self_cast")
  def class_id = column[Int]("class_id")
  def icon_url = column[Option[String]]("icon_url")

  def * = (id, name, cast_time, cooldown, spell_type, cast_type,
    spell_radius, spell_range, shape, self_cast, class_id, icon_url) <> (Spell.tupled, Spell.unapply)
}

class SpellEffects(tag: Tag) extends Table[SpellEffect](tag, Some("public"), "spell_effect") {
  def id = column[UUID]("id", O.PrimaryKey)
  def spell_id = column[UUID]("spell_id")
  def effect_type = column[Int]("effect_type")
  def damage_source = column[Int]("damage_source")
  def buff_source = column[Int]("buff_source")
  def percent_source_min = column[Int]("percent_source_min")
  def percent_source_max = column[Int]("percent_source_max")
  def flat_amount_min = column[Int]("flat_amount_min")
  def flat_amount_max = column[Int]("flat_amount_max")
  def dot_tick = column[Int]("dot_tick")
  def dot_duration = column[Int]("dot_duration")
  def buff_duration = column[Int]("buff_duration")
  def mechanic = column[Int]("mechanic")
  def school = column[Int]("school")
  def script_name = column[Option[String]]("script_name")
  def script_arguments = column[Option[String]]("script_arguments")
  def delta = column[Int]("delta")
  def max_stacks = column[Int]("max_stacks")

  def * = (id, spell_id, effect_type, damage_source, buff_source,
    percent_source_min, percent_source_max, flat_amount_min,
    flat_amount_max, dot_tick, dot_duration, buff_duration,
    mechanic, school, script_name, script_arguments, delta,
    max_stacks) <> (SpellEffect.tupled, SpellEffect.unapply)
}

class SpellTriggers(tag: Tag) extends Table[SpellTrigger](tag, Some("public"), "spell_trigger") {
  def id = column[UUID]("id", O.PrimaryKey)
  def spell_id = column[UUID]("spell_id")
  def trigger_spell_id = column[UUID]("trigger_spell_id")
  def chance = column[Double]("chance")
  def trigger_type = column[Int]("trigger_type")

  def * = (id, spell_id, trigger_spell_id, chance,
    trigger_type) <> (SpellTrigger.tupled, SpellTrigger.unapply)
}

class ClassSpells(tag: Tag) extends Table[ClassSpell](tag, Some("public"), "class_spell") {
  def id = column[UUID]("id", O.PrimaryKey)
  def spell_id = column[UUID]("spell_id")
  def class_id = column[Int]("class_id")
  def slot = column[Option[String]]("slot")

  def * = (id, spell_id, class_id, slot) <> (ClassSpell.tupled, ClassSpell.unapply)
}

