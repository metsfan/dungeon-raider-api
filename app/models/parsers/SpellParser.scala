package models.parsers

import anorm.SqlParser._
import anorm.{RowParser, ~}
import models.{SpellEffect, SpellTrigger, Spell}
import play.api.libs.json.Json._
import play.api.libs.json._
import scala.collection.mutable

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
    get[Double]("spell_radius") ~
    get[Double]("spell_range") ~
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
    get[Int]("school") ~
    get[Int]("duration") map {
      case id ~ spellId ~ minValue ~ maxValue ~ statType ~ effectType ~ school ~ duration => {
        SpellEffect(id, spellId, minValue, maxValue, statType, effectType, school, duration)
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

  def spellFormParser(data: JsValue, id: Int): Spell = {
    val effects = new mutable.MutableList[SpellEffect]

    val effectsData = (data \ "effects").as[List[JsValue]]
    effectsData foreach {
      effect => {
        val id = (effect \ "id").as[Int]
        val spell_id = (effect \ "spell_id").as[Int]
        val min_value = (effect \ "min_value").as[Int]
        val max_value = (effect \ "max_value").as[Int]
        val stat_type = (effect \ "stat_type").as[Int]
        val effect_type = (effect \ "effect_type").as[Int]
        val school = (effect \ "school").as[Int]
        val duration = (effect \ "duration").as[Int]

        effects += SpellEffect(id, spell_id, min_value, max_value, stat_type, effect_type, school, duration)
      }
    }

    val triggers = new mutable.MutableList[SpellTrigger]

    val triggersData = (data \ "triggers").as[List[JsValue]]
    triggersData foreach {
      trigger => {
        val id = (trigger \ "id").as[Int]
        val spell_id = (trigger \ "spell_id").as[Int]
        val trigger_spell_id = (trigger \ "trigger_spell_id").as[Int]
        val chance = (trigger \ "chance").as[Float]
        val trigger_type = (trigger \ "trigger_type").as[Int]

        triggers += SpellTrigger(id, spell_id, trigger_spell_id, chance, trigger_type)
      }
    }

    val name = (data \ "name").as[String]
    val cast_time = (data \ "cast_time").as[Int]
    val cooldown = (data \ "cooldown").as[Int]
    val spell_type = (data \ "spell_type").as[Int]
    val cast_type = (data \ "cast_type").as[Int]
    val radius = (data \ "radius").as[Double]
    val range = (data \ "range").as[Double]
    val shape = (data \ "shape").as[Int]

    Spell(id, name, cast_time, cooldown, spell_type, cast_type,
      radius, range, shape, effects.toList, triggers.toList)
  }

  def jsonify(spells: List[Spell]) = {
    toJson(spells)
  }

  def jsonify(spell: Option[Spell]) = {
    toJson(spell)
  }
}
