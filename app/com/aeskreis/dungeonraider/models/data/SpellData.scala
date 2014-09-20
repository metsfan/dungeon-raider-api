package com.aeskreis.dungeonraider.models.data

import play.api.Play.current
import scala.slick.driver.PostgresDriver.simple._
import play.api.db.slick.DB
import models._
import java.util.UUID
import com.aeskreis.dungeonraider.lib.Global
import play.api.libs.json.Json._
import play.api.libs.json._

/**
 * Created by Adam on 2/10/14.
 */
class SpellData extends BaseData {
  val spells = TableQuery[Spells]
  val spellEffects = TableQuery[SpellEffects]
  val spellTriggers = TableQuery[SpellTriggers]
  val classSpells = TableQuery[ClassSpells]
  val charClasses = TableQuery[CharClasses]

  def getById(id: UUID): Option[Spell] = {
    DB.withSession { implicit session =>
      val spell = spells.filter(_.id === id).firstOption

      if (spell.isDefined) {
        spell.get.effects = spellEffects.filter(_.spell_id === id).list
        spell.get.triggers = spellTriggers.filter(_.spell_id === id).list
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
      if (model.id == Global.UUIDZero) {
        model.id = UUID.randomUUID()
        spells.insert(model)

        classSpells.insert(ClassSpell(UUID.randomUUID(), model.id, model.class_id))
      } else {
        spells.filter(_.id === model.id).update(model)
      }

      model.effects foreach { effect =>
        if (effect.id == Global.UUIDZero) {
          effect.id = UUID.randomUUID()
          effect.spell_id = model.id
          spellEffects.insert(effect)
        } else {
          spellEffects.filter(_.id === effect.id).update(effect)
        }
      }

      model.triggers foreach { trigger =>
        if (trigger.id == Global.UUIDZero) {
          trigger.id = UUID.randomUUID()
          trigger.spell_id = model.id
          spellTriggers.insert(trigger)
        } else {
          spellTriggers.filter(_.id === trigger.id).update(trigger)
        }
      }

      Option[Spell](model)
    }
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

  def saveSlots(classId: Int, data: JsValue) {
    DB.withSession { implicit session =>
      data.as[List[JsValue]].foreach { spell =>
        val spellId = (spell \ "spell_id").as[UUID]
        val slot = (spell \ "slot").asOpt[String]

        val c = classSpells.filter(_.spell_id === spellId).first
        classSpells.filter(_.spell_id === spellId).update(ClassSpell(c.id, c.spell_id, c.class_id, slot))
      }
    }
  }

  def delete(id: UUID): Int = {
    val spell_id = id

    DB.withSession { implicit session =>
      spells.filter(_.id === spell_id).delete
      spellEffects.filter(_.spell_id === spell_id).delete
      spellTriggers.filter(_.spell_id === spell_id).delete
      classSpells.filter(_.spell_id === id).delete
      1
    }
  }

  def deleteEffect(id: UUID): Int = {
    DB.withSession { implicit session =>
      spellEffects.filter(_.id === id).delete
      1
    }
  }

  def deleteTrigger(id: UUID): Int = {
    DB.withSession { implicit session =>
      spellTriggers.filter(_.id === id).delete
      1
    }
  }
}
