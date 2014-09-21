package com.aeskreis.dungeonraider.controllers

import play.api.libs.json.Json._
import com.aeskreis.dungeonraider.models.data.SpellData
import play.api.mvc.Action
import play.api.libs.json.JsValue
import java.util.UUID
import com.aeskreis.dungeonraider.lib.json.SpellParser
import com.aeskreis.dungeonraider.models.Spell

object SpellController extends BaseController {
  val spellData = new SpellData()
  val parser = new SpellParser

  def list = Action {
    Ok(parser.toJsonArray(spellData.all(200))).as("application/json")
  }

  def listByClass(classId: Int) = Action {
    Ok(parser.toJsonArray(spellData.allForClass(classId))).as("application/json")
  }

  def get(id: UUID) = Action {
    Ok(parser.toJsonObject(spellData.getById(id).get)).as("application/json")
  }

  def create() = save(null)

  def save(id: UUID) = Action {
    implicit request => {

      val body = parseRequestJSON(request)
      var error = "An unknown error occured."
      var description = ""
      var result = ""
      var success = false

      if (body != null) {
        try {
          val original = spellData.getById(id)
          val saved = spellData.save(parser.toObject(original, body).get)
          if (saved.isDefined) {
            val classData = (body \ "char_class").asOpt[JsValue]
            if (classData.isDefined) {
              val isNew = id == null
              spellData.saveForClass(saved.get, classData.get, isNew)
            }
            result = parser.toJsonObject(saved.get).toString
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

  def saveSlots(classId: String) = Action { implicit request =>
    val body = parseRequestJSON(request)

    if (body != null) {
      spellData.saveSlots(classId.toInt, body)
    }
    Ok("")
  }

  def delete(id: UUID) = Action {
    val result = spellData.delete(id)
    Ok(toJson(Map("result" -> result))).as("application/json")
  }

  def deleteEffect(id: UUID) = Action {
    val result = spellData.deleteEffect(id)
    Ok(toJson(Map("result" -> result))).as("application/json")
  }

  def deleteTrigger(id: UUID) = Action {
    val result = spellData.deleteTrigger(id)
    Ok(toJson(Map("result" -> result))).as("application/json")
  }
}
