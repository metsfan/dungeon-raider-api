package lib.json

import anorm.SqlParser._
import anorm.RowParser
import models._
import play.api.libs.json.Json._
import play.api.libs.json._
import scala.collection.mutable
import scala.Some
import models.SpellEffect
import models.CharClass
import models.SpellTrigger
import play.api.libs.json.JsObject
import models.Spell
import java.util.UUID
import lib.Global

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/19/13
 * Time: 9:25 PM
 * To change this template use File | Settings | File Templates.
 */
class SpellParser extends JsonObjectParser[Spell] {
  implicit val charClassWrites = writes[CharClass]

  val spellEffectParser = new SpellEffectParser
  val spellTriggerParser = new SpellTriggerParser

  implicit val spellWrites = writes[Spell]
  implicit val spellReads = reads[Spell]

  def spellFormParser(data: JsValue, id: UUID): Spell = {
    val effects = (data \ "effects").as[List[JsValue]] map {
      effect => {
        val id = (effect \ "id").as[UUID]
        val spell_id = (effect \ "spell_id").as[UUID]
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
    val triggersData = (data \ "triggers").as[List[JsValue]]
    val triggers = triggersData map {
      trigger => {
        val id = (trigger \ "id").as[UUID]
        val spell_id = (trigger \ "spell_id").as[UUID]
        val trigger_spell_id = (trigger \ "trigger_spell_id").as[UUID]
        val chance = (trigger \ "chance").as[Float]
        val trigger_type = (trigger \ "trigger_type").as[Int]

        SpellTrigger(id, spell_id, trigger_spell_id, chance, trigger_type)
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

    val spell = Spell(id, name, castTime, cooldown, spellType, castType,
      radius, range, shape, selfCast, charClass.get.id, iconUrl)

    spell.effects = effects
    spell.triggers = triggers

    spell
  }

  override def toObject(obj: Option[Spell], data: JsValue): JsResult[Spell] = {
    val spell = _toObject(obj.getOrElse(Spell()), data, spellWrites, spellReads)
    if (spell.isSuccess) {
      val effectsJson = (data \ "effects").asOpt[List[JsValue]]
      if (effectsJson.isDefined) {
        spell.get.effects = effectsJson.get.map { effectJson =>
          val id = (effectJson \ "id").asOpt[UUID].getOrElse(Global.UUIDZero)
          val effectObj = if (obj.isDefined) {
            obj.get.effects.find(_.id == id)
          } else {
            None
          }

          spellEffectParser.toObject(effectObj, effectJson).get
        }
      }

      val triggersJson = (data \ "triggers").asOpt[List[JsValue]]
      if (triggersJson.isDefined) {
        spell.get.triggers = triggersJson.get.map { triggerJson =>
          val id = (triggerJson \ "id").asOpt[UUID].getOrElse(null)
          val triggerObj = if (obj.isDefined && id != null) {
            obj.get.triggers.find(_.id == id)
          } else {
            None
          }

          spellTriggerParser.toObject(triggerObj, triggerJson).get
        }
      }
    }

    spell
  }

  override def toJsonArray(objs: List[Spell]): JsValue = {
    _toJsonArray(objs, "spells", spellWrites)
  }

  override def toJsonObject(obj: Spell): JsValue = {
    _toJsonObject(obj, "spell", spellWrites)
  }

  override def getExtra(obj: Spell): JsObject = {
    spellEffectParser.toJsonArray(obj.effects).asInstanceOf[JsObject] ++
    spellTriggerParser.toJsonArray(obj.triggers).asInstanceOf[JsObject] ++
      (if (obj.classSpell.isDefined) {
      Json.obj("slot" -> obj.classSpell.get.slot)
    } else { Json.obj() })
  }
}
