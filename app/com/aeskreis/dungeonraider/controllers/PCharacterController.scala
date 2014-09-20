package com.aeskreis.dungeonraider.controllers

import models.data.{SpellData, PCharacterData}
import play.api.mvc.Action
import models.{Spell}
import java.util.UUID
import com.aeskreis.dungeonraider.lib.json.PCharacterParser

/**
 * Created by Adam on 2/10/14.
 */
object PCharacterController extends BaseController {

  val characterData = new PCharacterData
  val spellData = new SpellData

  val parser = new PCharacterParser

  def list(user_id: UUID) = Action {
    Ok(parser.toJsonArray(characterData.all(user_id))).as("application/json")
  }

  def get(id: UUID) = Action {
    val character = characterData.get(id)
    val spells = if (character.isDefined) {
      val classSpells = spellData.allForClass(character.get.class_id)
      val globalSpells = spellData.allForClass(1);
      character.get.spells = globalSpells ++ classSpells
    }

    Ok(parser.toJsonObject(character.get)).as("application/json")
  }

  def classList = Action {
    Ok(parser.jsonifyClasses(characterData.allClasses)).as("application/json")
  }
}
