package models.data

import play.api.Play.current
import models.query.SpellQuery
import scala.slick.driver.PostgresDriver.simple._
import play.api.db.slick.DB
import java.sql.Connection
import models._
import models.parsers.SpellParser
import play.api.libs.json.{JsArray, JsValue}
import java.util.UUID

/**
 * Created by Adam on 2/10/14.
 */
class SpellData extends BaseData with SpellParser {
  val spells = TableQuery[Spells]
  val spellEffects = TableQuery[SpellEffects]
  val spellTriggers = TableQuery[SpellTriggers]
  val classSpells = TableQuery[ClassSpells]
  val charClasses = TableQuery[CharClasses]

  def getById(id: String): Option[Spell] = {
    DB.withSession { implicit session =>
      val spell = spells.filter(_.id === id.toInt).firstOption

      if (spell.isDefined) {
        spell.get.effects = spellEffects.filter(_.spell_id === id.toInt).list
        spell.get.triggers = spellTriggers.filter(_.spell_id === id.toInt).list
      }

      spell
    }
  }

  def all(limit: Int): List[Spell] = {
    DB.withSession { implicit session =>
      val allSpells = spells.list.take(limit)

      val effects = spellEffects.list
      val triggers = spellTriggers.list

      allSpells foreach { spell =>
        spell.effects = effects.filter(_.spell_id == spell.id)
        spell.triggers = triggers.filter(_.spell_id == spell.id)
      }

      allSpells
    }
  }

  def allForClass(id: Int): List[Spell] = {
    DB.withSession { implicit session =>
      val q = (for {
        (s, c) <- spells innerJoin classSpells on (_.id === _.spell_id)
      } yield(s, c)).filter(_._2.class_id === id)

      val effects = (for {
        (e, c) <- spellEffects innerJoin classSpells on (_.spell_id === _.spell_id)
      } yield(e, c)).filter(_._2.class_id === id).list map(_._1)

      val triggers = (for {
        (t, c) <- spellTriggers innerJoin classSpells on (_.spell_id === _.spell_id)
      } yield(t, c)).filter(_._2.class_id === id).list map(_._1)

      q.list map { case (spell, classSpell) =>
        spell.effects = effects.filter(_.spell_id == spell.id)
        spell.triggers = triggers.filter(_.spell_id == spell.id)
        spell.classSpell = Some(classSpell)
        spell.charClass = charClasses.filter(_.id === spell.class_id).firstOption

        spell
      }
    }
  }

  def allForCharacterById(id: String): List[Spell] = {
    /*DB.withConnection { implicit conn =>
      val effects = SQL(SpellQuery.selectSpellEffectsByCharacter)
        .on("char_id" -> id.toInt)
        .as(spellEffectRowParser *).toList;

      val triggers = SQL(SpellQuery.selectSpellTriggersByCharacter)
        .on("char_id" -> id.toInt)
        .as(spellTriggerRowParser *).toList;

      SQL(SpellQuery.selectSpellByCharacter)
        .on("char_id" -> id.toInt)
        .as(spellRowParser(effects, triggers) *).toList;
    }*/
    DB.withSession { implicit session =>
      List[Spell]()
    }
  }

  def save(model: Spell): Option[Spell] = {
    DB.withSession { implicit session =>
      if (model.id == null) {
        spells.insert(model)
      } else {
        spells.update(model)
      }

      Option[Spell](model)
    }
    /*DB.withConnection {
      implicit conn => {
        var spellFields: Seq[(Any, ParameterValue[_])] = Seq(
          "name" -> model.name,
          "cast_time" -> model.cast_time,
          "cooldown" -> model.cooldown,
          "spell_type" -> model.spell_type,
          "cast_type" -> model.cast_type,
          "radius" -> model.radius,
          "range" -> model.range,
          "shape" -> model.shape,
          "self_cast" -> model.self_cast,
          "class_id" -> (if(model.char_class.isDefined) model.char_class.get.id else 0),
          "icon_url" -> model.icon_url
        )

        if (model.id > 0) {
          spellFields ++= Seq[(Any, ParameterValue[_])]("spell_id" -> model.id)
          SQL(SpellQuery.updateSpell).on(spellFields: _*).executeUpdate()
        } else {
          SQL(SpellQuery.insertSpell).on(spellFields: _*).executeInsert().map {
            id => model.id = id.toInt
          }
        }

        model.effects.foreach(effect => {
          if(effect.spell_id == 0) {
            effect.spell_id = model.id
          }

          var effectFields: Seq[(Any, ParameterValue[_])] = Seq(
            "spell_id" -> effect.spell_id,
            "effect_type" -> effect.effect_type,
            "damage_source" -> effect.damage_source,
            "buff_source" -> effect.buff_source,
            "percent_source_min" -> effect.percent_source_min,
            "percent_source_max" -> effect.percent_source_max,
            "flat_amount_min" -> effect.flat_amount_min,
            "flat_amount_max" -> effect.flat_amount_max,
            "dot_tick" -> effect.dot_tick,
            "dot_duration" -> effect.dot_duration,
            "buff_duration" -> effect.buff_duration,
            "mechanic" -> effect.mechanic,
            "school" -> effect.school,
            "script_name" -> effect.script_name,
            "script_arguments" -> effect.script_arguments,
            "delta" -> effect.delta,
            "max_stacks" -> effect.max_stacks
          )
          if (effect.id > 0) {
            effectFields ++= Seq[(Any, ParameterValue[_])]("effect_id" -> effect.id)
            SQL(SpellQuery.updateSpellEffect).on(effectFields: _*).executeUpdate()
          } else {
            SQL(SpellQuery.insertSpellEffect).on(effectFields: _*).executeInsert().map({
              id => effect.id = id.toInt
            })
          }
        })

        model.triggers.foreach(trigger => {
          if(trigger.spell_id == 0) {
            trigger.spell_id = model.id
          }

          var triggerFields: Seq[(Any, ParameterValue[_])] = Seq(
            "spell_id" -> trigger.spell_id,
            "trigger_spell_id" -> trigger.trigger_spell_id,
            "chance" -> trigger.chance,
            "trigger_type" -> trigger.trigger_type
          )
          if (trigger.id > 0) {
            triggerFields ++= Seq[(Any, ParameterValue[_])]("trigger_id" -> trigger.id)
            SQL(SpellQuery.updateSpellTrigger).on(triggerFields: _*).executeUpdate()
          } else {
            SQL(SpellQuery.insertSpellTrigger).on(triggerFields: _*).executeInsert().map({
              id => trigger.id = id.toInt
            })
          }
        })

        Option[Spell](model)
      }
    }*/
  }

  def saveForClass(spell: Spell, classData: JsValue, isNew: Boolean) {
    /*DB.withConnection { implicit conn =>
      val class_id = (classData \ "id").as[Int]

      val fields: Seq[(Any, ParameterValue[_])] = Seq(
        "spell_id" -> spell.id,
        "class_id" -> class_id
      )

      if (isNew) {
        SQL(SpellQuery.insertSpellClass).on(fields: _*).execute()
      } else {
        //SQL(SpellQuery.updateSpellClass).on(fields: _*).execute()
      }
    }*/
  }

  def saveSlots(classId: String, data: JsValue) {
    /*DB.withConnection { implicit conn =>
      val spells = data.as[List[JsValue]]

      spells.foreach { spell =>
        val spellId = (spell \ "spell_id").as[Int]
        val slot = (spell \ "slot").as[String]

        SQL(SpellQuery.updateSpellSlot).on(
          "spell_id" -> spellId,
          "class_id" -> classId.toInt,
          "slot" -> slot
        ).executeUpdate
      }

      val updateSql = SQL(SpellQuery.updateSpellSlot)
      val updateStmt = (updateSql.asBatch /: params) (
        (sql, elem) => sql.addBatchParams(elem)
      )

      updateStmt.execute
    }*/
  }

  def delete(id: String): Int = {
    val spell_id = id.toInt;

    DB.withSession { implicit session =>
      spells.filter(_.id === spell_id).delete
      spellEffects.filter(_.spell_id === spell_id).delete
      spellTriggers.filter(_.spell_id === spell_id).delete

      1
    }
  }

  def deleteEffect(id: String): Int = {
    DB.withSession { implicit session =>
      spellEffects.filter(_.id === id.toInt).delete
      1
    }
  }

  def deleteTrigger(id: String): Int = {
    DB.withSession { implicit session =>
      spellTriggers.filter(_.id === id.toInt).delete
      1
    }
  }

  /** Private */

  /*def getAllEffects(implicit conn: Connection): List[SpellEffect] = {
    SQL(SpellQuery.selectAllEffects).as(spellEffectRowParser *).toList
  }

  def getAllTriggers(implicit conn: Connection): List[SpellTrigger] = {
    SQL(SpellQuery.selectAllTriggers).as(spellTriggerRowParser *).toList
  }

  def getEffectsForSpell(implicit conn: Connection, spellId: String): List[SpellEffect] = {
    SQL(SpellQuery.selectEffectsForSpell).on("spellId" -> spellId.toInt).as(spellEffectRowParser *).toList
  }

  def getTriggersForSpell(implicit conn: Connection, spellId: String): List[SpellTrigger] = {
    SQL(SpellQuery.selectTriggersForSpell).on("spellId" -> spellId.toInt).as(spellTriggerRowParser *).toList
  }*/
}
