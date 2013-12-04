package models.data

import anorm._
import models.{SpellTrigger, SpellEffect, Spell, SpellDataComponent}
import models.data.query.SpellQuery
import models.parsers.SpellParser
import play.api.db.DB
import play.api.Play.current
import java.sql.Connection

class SpellData extends SpellDataComponent with SpellParser {

  def getById(id: String): Option[Spell] = {
    DB.withConnection {
      implicit conn => {
        val effects = getEffectsForSpell(conn, id)
        val triggers = getTriggersForSpell(conn, id)

        SQL(SpellQuery.selectById).on("spellId" -> id).as(spellRowParser(effects, triggers) *).headOption
      }
    }
  }

  def all(limit: Int): List[Spell] = {
    DB.withConnection {
      implicit conn => {
        val effects = getAllEffects(conn)
        val triggers = getAllTriggers(conn)
        SQL(SpellQuery.selectAll).as(spellRowParser(effects, triggers) *).toList
      }
    }
  }

  def save(model: Spell): Option[Spell] = {
    DB.withConnection {
      implicit conn => {
        var spellFields: Seq[(Any, ParameterValue[_])] = Seq(
          "name" -> model.name,
          "cast_time" -> model.cast_time,
          "cooldown" -> model.cooldown,
          "spell_type" -> model.spell_type,
          "cast_type" -> model.cast_type,
          "radius" -> model.radius,
          "range" -> model.range,
          "shape" -> model.shape
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
            "min_value" -> effect.min_value,
            "max_value" -> effect.max_value,
            "stat_type" -> effect.stat_type,
            "effect_type" -> effect.effect_type,
            "school" -> effect.school
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
    }

  }

  def delete(id: String): Int = {
    DB.withConnection {
      implicit conn => {
        val spell_id = id.toInt;
        SQL(SpellQuery.deleteSpell).on("id" -> spell_id).executeUpdate()
        SQL(SpellQuery.deleteEffects).on("spell_id" -> spell_id).executeUpdate()
        SQL(SpellQuery.deleteTriggers).on("spell_id" -> spell_id).executeUpdate()

        1
      }
    }
  }

  /** Private */

  def getAllEffects(implicit conn: Connection): List[SpellEffect] = {
    SQL(SpellQuery.selectAllEffects).as(spellEffectRowParser *).toList
  }

  def getAllTriggers(implicit conn: Connection): List[SpellTrigger] = {
    SQL(SpellQuery.selectAllTriggers).as(spellTriggerRowParser *).toList
  }

  def getEffectsForSpell(implicit conn: Connection, spellId: String): List[SpellEffect] = {
    SQL(SpellQuery.selectEffectsForSpell).on("spellId" -> spellId).as(spellEffectRowParser *).toList
  }

  def getTriggersForSpell(implicit conn: Connection, spellId: String): List[SpellTrigger] = {
    SQL(SpellQuery.selectTriggersForSpell).on("spellId" -> spellId).as(spellTriggerRowParser *).toList
  }
}