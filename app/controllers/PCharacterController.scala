package controllers

import models.parsers.PCharacterParser
import models.data.{SpellData, PCharacterData}
import play.api.mvc.Action
import models.Spell
import java.util.UUID

/**
 * Created by Adam on 2/10/14.
 */
object PCharacterController extends BaseController with PCharacterParser {

  val characterData = new PCharacterData
  val spellData = new SpellData

  def list(user_id: UUID) = Action {
    Ok(jsonify(characterData.all(user_id))).as("application/json")
  }

  def get(id: UUID) = Action {
    val character = characterData.get(id)
    val spells = if (character.isDefined) {
      val classSpells = spellData.allForClass(character.get.class_id.toString)
      val globalSpells = spellData.allForClass("1");
      globalSpells ++ classSpells
    } else {
      List[Spell]()
    }

    Ok(jsonify(character, spells)).as("application/json")
  }

  def classList = Action {
    Ok(jsonifyClasses(characterData.allClasses)).as("application/json")
  }
}
