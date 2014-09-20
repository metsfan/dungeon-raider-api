package com.aeskreis.dungeonraider.lib.json

import play.api.libs.json.Json._
import play.api.libs.json._
import models.NPCharacter

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/27/13
 * Time: 11:08 PM
 * To change this template use File | Settings | File Templates.
 */
class NPCharacterParser extends JsonObjectParser[NPCharacter] {
  implicit val charWrites = writes[NPCharacter]
  implicit val charReads = reads[NPCharacter]

  val spellParser = new SpellParser

  override def toObject(obj: Option[NPCharacter], data: JsValue): JsResult[NPCharacter] = {
    val character = _toObject(obj.getOrElse(NPCharacter()), data, charWrites, charReads)

    /*val spellsData = (data \ "spells").asOpt[List[JsValue]]
    if (spellsData.isDefined) {
      val spells = spellsData.get map(spell => {
        val spell_id = (spell \ "id").as[UUID]
        spellData.spellFormParser(spell, spell_id)
      })
      character.get.spells = spells;
    }*/

    character
  }

  override def toJsonArray(objs: List[NPCharacter]): JsValue = {
    _toJsonArray(objs, "characters", charWrites)
  }

  override def toJsonObject(obj: NPCharacter): JsValue = {
    _toJsonObject(obj, "character", charWrites)
  }

  override def getExtra(obj: NPCharacter): JsObject = {
    spellParser.toJsonArray(obj.spells).asInstanceOf[JsObject]
  }
}
