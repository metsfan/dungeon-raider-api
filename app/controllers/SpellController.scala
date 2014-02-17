package controllers

import play.api.libs.json.Json._
import models.parsers.SpellParser
import models.data.SpellData
import play.api.mvc.Action

object SpellController extends BaseController with SpellParser {
  val spellData = new SpellData()

  def list = Action {
    Ok(jsonify(spellData.all(200))).as("application/json")
  }

  def listByClass(classId: String) = Action {
    Ok(jsonify(spellData.allForClass(classId))).as("application/json")
  }

  def get(id: String) = Action {
    Ok(jsonify(spellData.getById(id))).as("application/json")
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
          val saved = spellData.save(spellFormParser(body, id.toInt))
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
    val result = spellData.delete(id)
    Ok(toJson(Map("result" -> result))).as("application/json")
  }
}
