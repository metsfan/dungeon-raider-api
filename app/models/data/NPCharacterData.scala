package models.data

import models.parsers.{SpellParser, NPCharacterParser}
import play.api.db.DB
import play.api.Play.current
import anorm._
import models.query.NPCharacterQuery
import anorm.ParameterValue
import models._

/**
 * Created by Adam on 2/10/14.
 */
class NPCharacterData extends BaseData with NPCharacterParser {
  val spellData = new SpellData

  def getById(id: String): Option[NPCharacter] = {
    DB.withConnection {
      implicit conn => {
        //val spells = SQL(NPCharacterQuery.selectSpellsById).on("id" -> id.toInt).as(charSpellRowParser *).toList
        val spells = spellData.allForCharacterById(id)
        val charSpells = SQL(NPCharacterQuery.selectSpellsById).on("id" -> id.toInt).as(charSpellRowParser(spells) *).toList

        SQL(NPCharacterQuery.selectById).on("id" -> id.toInt).as(charRowParser(charSpells) *).headOption
      }
    }
  }

  def all(limit: Int): List[NPCharacter] = {
    DB.withConnection {
      implicit conn => {
        val spells = spellData.all(1000)
        val charSpells = SQL(NPCharacterQuery.selectSpells).as(charSpellRowParser(spells) *).toList

        SQL(NPCharacterQuery.selectAll).as(charRowParser(charSpells) *).toList
      }
    }
  }

  def save(model: NPCharacter): Option[NPCharacter] = {
    DB.withConnection {
      implicit conn => {
        /*var charFields: Seq[(Any, ParameterValue[_])] = Seq(
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
          SQL(NPCharacterQuery.updateCharacter).on(charFields: _*).executeUpdate()
        } else {
          SQL(NPCharacterQuery.insertCharacter).on(charFields: _*).executeInsert().map {
            id => model.id = id.toInt
          }
        }

        model.spells.foreach {
          spell => {
            var spellFields: Seq[(Any, ParameterValue[_])] = Seq(
              "spell_id" -> spell.id,
              "char_id" -> model.id
            )

            if (spell.id > 0) {
              spellFields ++= Seq[(Any, ParameterValue[_])]("id" -> spell.id)
              SQL(NPCharacterQuery.updateSpell).on(spellFields: _*).executeUpdate()
            } else {
              SQL(NPCharacterQuery.insertSpell).on(spellFields: _*).executeInsert().map {
                id => spell.id = id.toInt
              }
            }
          }
        }*/

        Option[NPCharacter](model)
      }
    }
  }

  def delete(id: String): Int = {
    DB.withConnection {
      implicit conn => {
        SQL(NPCharacterQuery.deleteSpells).on("id" -> id).executeUpdate
        SQL(NPCharacterQuery.deleteCharacter).on("id" -> id).executeUpdate
      }
    }
  }
}
