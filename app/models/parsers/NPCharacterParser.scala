package models.parsers

import anorm._
import anorm.SqlParser._
import models.{NPCharacterSpell, NPCharacter, Spell}
import play.api.libs.json.Json._
import play.api.libs.json._
import scala.collection.mutable

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/27/13
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
trait NPCharacterParser {

  implicit val charSpellWrites = writes[NPCharacterSpell]
  implicit val charWrites = writes[NPCharacter]

  def charSpellRowParser: RowParser[NPCharacterSpell] = {
    get[Int]("id") ~
    get[Int]("spell_id") ~
    get[Int]("char_id") map {
      case id ~ spellId ~ charId => {
        NPCharacterSpell(id, spellId, charId)
      }
    }
  }

  def charRowParser(spells: List[NPCharacterSpell]): RowParser[NPCharacter] = {
    get[Int]("id") ~
    get[String]("name") ~
    get[Int]("class_id") ~
    get[String]("model") ~
    get[Int]("health") ~
    get[Int]("race") ~
    get[Int]("level") ~
    get[Int]("hostility") ~
    get[Int]("faction") ~
    get[Double]("agro_radius") map {
      case id ~ name ~ classId ~ model ~ health ~ race ~ level ~
        hostility ~ faction ~ agro_radius => {
        val filteredSpells = spells.filter(spell => spell.char_id == id)
        NPCharacter(id, name, health, race, level, classId, model,
          hostility, faction, agro_radius, filteredSpells)
      }
    }
  }

  def charFormParser(data: JsValue, id: Int): NPCharacter = {
    var spells = new mutable.MutableList[NPCharacterSpell];

    val spellsData = (data \ "spells").as[List[JsValue]]
    spellsData foreach {
      spell => {
        val id = (spell \ "id").as[Int]
        val spell_id = (spell \ "spell_id").as[Int]
        val char_id = (spell \ "char_id").as[Int]

        spells += NPCharacterSpell(id, spell_id, char_id)
      }
    }

    val id = (data \ "id").as[Int]
    val name = (data \ "name").as[String]
    val health = (data \ "health").as[Int]
    val race = (data \ "race").as[Int]
    val level = (data \ "level").as[Int]
    val class_id = (data \ "class_id").as[Int]
    val model = (data \ "model").as[String]
    val hostility = (data \ "hostility").as[Int]
    val faction = (data \ "faction").as[Int]
    val agro_radius = (data \ "agro_radius").as[Double]

    NPCharacter(id, name, health, race, level, class_id, model, hostility, faction, agro_radius, spells.toList)
  }

  def jsonify(npchars: List[NPCharacter]) = {
    toJson(npchars)
  }

  def jsonify(npchar: Option[NPCharacter]) = {
    toJson(npchar)
  }
}
