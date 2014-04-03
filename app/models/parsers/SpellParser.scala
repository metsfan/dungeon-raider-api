package models.parsers

import anorm.SqlParser._
import anorm.{RowParser, ~}
import models.{CharClass, SpellEffect, SpellTrigger, Spell}
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

  implicit val charClassWrites = writes[CharClass]
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
    get[Int]("shape") ~
    get[Boolean]("self_cast") ~
    get[Option[String]]("icon_url") ~
    get[Option[Int]]("class_id") ~
    get[Option[String]]("class_name") ~
    get[Option[String]]("slot") map {
      case id ~ name ~ castTime ~ cooldown ~ spellType ~ castType ~
        radius ~ range ~ shape ~ selfCast ~ iconUrl ~ classId ~ className ~ slot => {
        val filteredEffects = effects.filter(effect => effect.spell_id == id)
        val filteredTriggers = triggers.filter(trigger => trigger.spell_id == id)
        val charClass = if(classId.isDefined) Some(CharClass(classId.get, className.get)) else None
        Spell(id, name, castTime, cooldown, spellType, castType,
          radius, range, shape, selfCast, charClass, slot, iconUrl,
          filteredEffects, filteredTriggers)
      }
    }
  }

  def spellEffectRowParser: RowParser[SpellEffect] = {
    get[Int]("id") ~
    get[Int]("spell_id") ~
    get[Int]("effect_type") ~
    get[Int]("damage_source") ~
    get[Int]("buff_source") ~
    get[Int]("percent_source_min") ~
    get[Int]("percent_source_max") ~
    get[Int]("flat_amount_min") ~
    get[Int]("flat_amount_max") ~
    get[Int]("dot_tick") ~
    get[Int]("dot_duration") ~
    get[Int]("buff_duration") ~
    get[Int]("mechanic") ~
    get[Int]("school") ~
    get[Option[String]]("script_name") ~
    get[Option[String]]("script_arguments") ~
    get[Int]("delta") ~
    get[Int]("max_stacks") map {
      case id ~ spellId ~ effectType ~ damageSource ~ buffSource ~
        percentSourceMin ~ percentSourceMax ~ flatAmountMin ~ flatAmountMax ~
        dotTick ~ dotDuration ~ buffDuration ~ mechanic ~ school ~
        scriptName ~ scriptArgument ~ delta ~ maxStacks => {
        SpellEffect(id, spellId, effectType, damageSource, buffSource,
        percentSourceMin, percentSourceMax, flatAmountMin, flatAmountMax, dotTick,
        dotDuration, buffDuration, mechanic, school, scriptName, scriptArgument, delta, maxStacks)
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
    val effects = (data \ "effects").as[List[JsValue]] map {
      effect => {
        val id = (effect \ "id").as[Int]
        val spell_id = (effect \ "spell_id").as[Int]
        val effect_type = (effect \ "effect_type").as[Int]
        val damage_source = (effect \ "damage_source").asOpt[Int].getOrElse(0)
        val buff_source = (effect \ "buff_source").asOpt[Int].getOrElse(0)
        val percent_source_min = (effect \ "percent_source_min").asOpt[Int].getOrElse(0)
        val percent_source_max = (effect \ "percent_source_max").asOpt[Int].getOrElse(0)
        val flat_amount_min = (effect \ "flat_amount_min").asOpt[Int].getOrElse(0)
        val flat_amount_max = (effect \ "flat_amount_max").asOpt[Int].getOrElse(0)
        val dot_tick = (effect \ "dot_tick").asOpt[Int].getOrElse(0)
        val dot_duration = (effect \ "dot_duration").asOpt[Int].getOrElse(0)
        val buff_duration = (effect \ "buff_duration").asOpt[Int].getOrElse(0)
        val mechanic = (effect \ "mechanic").asOpt[Int].getOrElse(0)
        val school = (effect \ "school").asOpt[Int].getOrElse(0)
        val script_name = (effect \ "script_name").asOpt[String]
        val script_arguments = (effect \ "script_arguments").asOpt[String]
        val delta = (effect \ "delta").as[Int]
        val max_stacks = (effect \ "max_stacks").as[Int]

        SpellEffect(id, spell_id, effect_type, damage_source, buff_source,
        percent_source_min, percent_source_max, flat_amount_min, flat_amount_max,
        dot_tick, dot_duration, buff_duration, mechanic, school, script_name,
        script_arguments, delta, max_stacks)
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
    val castTime = (data \ "cast_time").as[Int]
    val cooldown = (data \ "cooldown").as[Int]
    val spellType = (data \ "spell_type").as[Int]
    val castType = (data \ "cast_type").as[Int]
    val radius = (data \ "radius").as[Double]
    val range = (data \ "range").as[Double]
    val shape = (data \ "shape").as[Int]
    val selfCast = (data \ "self_cast").as[Boolean]
    val iconUrl = (data \ "icon_url").asOpt[String]
    val slot = (data \ "slot").asOpt[String]

    val charClassData = (data \ "char_class").asOpt[JsValue]
    val charClass = if (charClassData.isDefined) {
      val classId = (charClassData.get \ "id").as[Int]
      val className = (charClassData.get \ "name").as[String]


      Some(CharClass(classId, className))
    } else {
      None
    }

    Spell(id, name, castTime, cooldown, spellType, castType,
      radius, range, shape, selfCast, charClass, slot, iconUrl,
      effects, triggers.toList)
  }

  def jsonify(spells: List[Spell]) = {
    toJson(spells)
  }

  def jsonify(spell: Option[Spell]) = {
    toJson(spell)
  }
}
