package models.parsers

import anorm.SqlParser._
import anorm.{RowParser, ~}
import models.{SpellEffect, SpellTrigger, Spell}
import play.api.libs.json.Json._
import play.api.libs.json._

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/19/13
 * Time: 9:25 PM
 * To change this template use File | Settings | File Templates.
 */
trait SpellParser {

  implicit val spellTriggerWrites = writes[SpellTrigger]
  implicit val spellEffectWrites = writes[SpellEffect]
  implicit val spellWrites = writes[Spell]

  def spellRowParser(effects: List[SpellEffect], triggers: List[SpellTrigger]): RowParser[Spell] = {
    get[Int]("id") ~
    get[String]("name") ~
    get[Int]("cast_time") ~
    get[Int]("cooldown") ~
    get[Int]("spell_type") ~
    get[Int]("cast_type") ~
    get[Double]("radius") ~
    get[Double]("range") ~
    get[Int]("shape") map {
      case id ~ name ~ castTime ~ cooldown ~ spellType ~ castType ~
        radius ~ range ~ shape => {
        val filteredEffects = effects.filter(effect => effect.spell_id == id)
        val filteredTriggers = triggers.filter(trigger => trigger.spell_id == id)
        Spell(id, name, castTime, cooldown, spellType, castType, radius, range, shape, filteredEffects, filteredTriggers)
      }
    }
  }

  def spellEffectRowParser: RowParser[SpellEffect] = {
    get[Int]("id") ~
    get[Int]("spell_id") ~
    get[Int]("min_value") ~
    get[Int]("max_value") ~
    get[Int]("stat_type") ~
    get[Int]("effect_type") ~
    get[Int]("school") map {
      case id ~ spellId ~ minValue ~ maxValue ~ statType ~ effectType ~ school => {
        SpellEffect(id, spellId, minValue, maxValue, statType, effectType, school)
      }
    }
  }

  def spellTriggerRowParser: RowParser[SpellTrigger] = {
    get[Int]("id") ~
    get[Int]("spell_id") ~
    get[Int]("trigger_spell_id") ~
    get[Double]("chance") ~
    get[Int]("trigger_type") map {
      case id ~ spellId ~ triggerSpellId ~ chance ~ triggerType => {
        SpellTrigger(id, spellId, triggerSpellId, chance, triggerType)
      }
    }
  }

  def jsonify(spells: List[Spell]) = {
    toJson(spells)
  }

  def jsonify(spell: Option[Spell]) = {
    toJson(spell)
  }
}
