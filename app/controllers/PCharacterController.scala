package controllers

import models.parsers.PCharacterParser
import models.data.PCharacterData
import play.api.mvc.Action

/**
 * Created by Adam on 2/10/14.
 */
object PCharacterController extends BaseController with PCharacterParser {

  val characterData = new PCharacterData

  def list(user_id: String) = Action {
    Ok(jsonify(characterData.all(user_id))).as("application/json")
  }

  def get(id: String) = Action {
    Ok(jsonify(characterData.get(id))).as("application/json")
  }

  def classList = Action {
    Ok(jsonifyClasses(characterData.allClasses)).as("application/json")
  }
}
