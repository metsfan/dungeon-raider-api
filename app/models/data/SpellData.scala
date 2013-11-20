package models.data

import anorm._
import models.{SpellTrigger, SpellEffect, Spell, SpellDataComponent}
import models.data.query.SpellQuery
import models.parsers.SpellParser
import play.api.db.DB
import play.api.Play.current
import java.sql.Connection

class SpellData extends SpellDataComponent with SpellParser {
  def add(id: String, model: Spell): Option[Spell] = ???

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

  def update(model: Spell): Option[Spell] = ???

  def delete(id: String): Int = ???

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