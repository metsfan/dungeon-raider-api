package models.data

import play.api.Play.current
import models.query.NPCharacterQuery
import models._
import scala.slick.driver.PostgresDriver.simple._
import play.api.db.slick.DB
import java.util.UUID
import lib.json.NPCharacterParser

/**
 * Created by Adam on 2/10/14.
 */
class NPCharacterData extends BaseData {
  val spellData = new SpellData

  val npcharacters = TableQuery[NPCharacters]
  val npcharacterSpells = TableQuery[NPCharacterSpells]

  def getById(id: UUID): Option[NPCharacter] = {
    if (id != null) {
      DB.withSession { implicit session =>
        npcharacters.filter(_.id === id).firstOption
      }
    } else {
      None
    }
  }

  def all(limit: Int): List[NPCharacter] = {
    DB.withSession { implicit session =>
      val characters = npcharacters.list

      val spells = spellData.all(1000)
      val npcSpells = npcharacterSpells.list

      characters foreach { character =>
        val curSpells = npcSpells.filter(_.char_id == character.id)
        character.spells = curSpells map { npcSpell =>
          spells.find(_.id == npcSpell.spell_id).get
        }
      }

      characters
    }
  }

  def save(model: NPCharacter): Option[NPCharacter] = {
    DB.withSession { implicit session =>
      if (model.id == null) {
        npcharacters.insert(model)
      } else {
        npcharacters.filter(_.id === model.id).update(model)
      }
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

  def delete(id: UUID): Int = {
    /*DB.withConnection {
      implicit conn => {
        SQL(NPCharacterQuery.deleteSpells).on("id" -> id).executeUpdate
        SQL(NPCharacterQuery.deleteCharacter).on("id" -> id).executeUpdate
      }
    }*/
    0
  }
}
