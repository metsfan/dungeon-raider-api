package controllers

import play.api.mvc._
import models.parsers.NPCharacterParser
import models.data.{SpellData, NPCharacterData}
import play.api.libs.json.Json._
import java.util.NoSuchElementException

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/27/13
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
object NPCharacterController extends BaseController with NPCharacterParser {
  val characterData = new NPCharacterData
  val spellData = new SpellData

  def list = Action {
    Ok(jsonify(characterData.all(200))).as("application/json")
  }

  def get(id: String) = Action {
    Ok(jsonify(characterData.getById(id))).as("application/json")
  }

  def save(id: String) = Action {
    implicit request => {
      val body = parseRequestJSON(request)
      var error = "An unknown error occured."
      var description = ""
      var result = ""
      var success = false

      if (body != null) {
        try {
          val saved = characterData.save(charFormParser(body, id.toInt, spellData))
          if (saved.isDefined) {
            result = toJson(saved).toString
            success = true
          }
        } catch {
          case e: NoSuchElementException => {
            error = "Missing Field."
            description = "A required field was not provided. " + e.getMessage
            e.printStackTrace()
          }

          case e: IndexOutOfBoundsException => {
            error = "Not enough elements."
            description = "Probably num_effects or num_triggers is greater than the number of records provided. Index: " + e.getMessage
            e.printStackTrace()
          }
        }
      }

      if (!success) {
        result = toJson(Map(
          "success" -> "false",
          "error" -> error,
          "description" -> description
        )).toString
      }

      Ok(result).as("application/json")
    }
  }

  def delete(id: String) = Action {
    val result = characterData.delete(id)
    Ok(toJson(Map("result" -> result))).as("application/json")
  }
}
