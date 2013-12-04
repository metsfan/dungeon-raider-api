package models.data

import anorm._
import models.{NPCharacter, CharacterDataComponent}
import models.parsers.CharacterParser
import models.data.query.{CharacterQuery}
import play.api.db.DB
import play.api.Play.current

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/28/13
 * Time: 10:13 AM
 * To change this template use File | Settings | File Templates.
 */
class CharacterData extends CharacterDataComponent with CharacterParser {

  def getById(id: String): Option[NPCharacter] = {
    DB.withConnection {
      implicit conn => {
        val spells = SQL(CharacterQuery.selectSpellsById).on("id" -> id).as(charSpellRowParser *).toList

        SQL(CharacterQuery.selectById).on("id" -> id).as(charRowParser(spells) *).headOption
      }
    }
  }

  def all(limit: Int): List[NPCharacter] = {
    DB.withConnection {
      implicit conn => {
        val spells = SQL(CharacterQuery.selectSpells).as(charSpellRowParser *).toList

        SQL(CharacterQuery.selectAll).as(charRowParser(spells) *).toList
      }
    }
  }

  def save(model: NPCharacter): Option[NPCharacter] = {
    DB.withConnection {
      implicit conn => {
        var charFields: Seq[(Any, ParameterValue[_])] = Seq(
          "name" -> model.name,
          "health" -> model.health,
          "race" -> model.race,
          "level" -> model.level,
          "class_id" -> model.class_id,
          "model" -> model.model,
          "hostility" -> model.hostility,
          "faction" -> model.faction,
          "agro_radius" -> model.agro_radius
        )

        if (model.id > 0) {
          charFields ++= Seq[(Any, ParameterValue[_])]("id" -> model.id)
          SQL(CharacterQuery.updateCharacter).on(charFields: _*).executeUpdate()
        } else {
          SQL(CharacterQuery.insertCharacter).on(charFields: _*).executeInsert().map {
            id => model.id = id.toInt
          }
        }

        model.spells.foreach {
          spell => {
            var spellFields: Seq[(Any, ParameterValue[_])] = Seq(
              "spell_id" -> spell.spell_id,
              "char_id" -> spell.char_id
            )

            if (spell.id > 0) {
              spellFields ++= Seq[(Any, ParameterValue[_])]("id" -> spell.id)
              SQL(CharacterQuery.updateSpell).on(spellFields: _*).executeUpdate()
            } else {
              SQL(CharacterQuery.insertSpell).on(spellFields: _*).executeInsert().map {
                id => spell.id = id.toInt
              }
            }
          }
        }

        Option[NPCharacter](model)
      }
    }
  }

  def delete(id: String): Int = {
    DB.withConnection {
      implicit conn => {
        SQL(CharacterQuery.deleteSpells).on("id" -> id).executeUpdate
        SQL(CharacterQuery.deleteCharacter).on("id" -> id).executeUpdate
      }
    }
  }
}
