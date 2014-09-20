package com.aeskreis.dungeonraider.controllers

import play.api.mvc._
import models.data.{SpellData, NPCharacterData}
import play.api.libs.json.Json._
import java.util.{UUID, NoSuchElementException}
import models.NPCharacter
import com.aeskreis.dungeonraider.lib.json.{NPCharacterParser, JsonObjectParser}

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/27/13
 * Time: 11:01 PM
 * To change this template use File | Settings | File Templates.
 */
object NPCharacterController extends BaseController {
  val characterData = new NPCharacterData
  val spellData = new SpellData

  implicit val objWrites = writes[NPCharacter]
  implicit val objReads = reads[NPCharacter]

  val parser = new NPCharacterParser

  def list = Action {
    Ok(parser.toJsonArray(characterData.all(200))).as("application/json")
  }

  def get(id: UUID) = Action {
    Ok(parser.toJsonObject(characterData.getById(id).get)).as("application/json")
  }

  def save(id: UUID) = Action {
    implicit request => {
      val body = parseRequestJSON(request)
      var error = "An unknown error occured."
      var description = ""
      var result = ""
      var success = false

      if (body != null) {
        try {
          val original = characterData.getById(id)
          val obj = parser.toObject(original, body)
          //val saved = characterData.save(charFormParser(body, id, spellData, character))
          if (obj.isSuccess) {
            result = toJson(obj.get).toString
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

  def delete(id: UUID) = Action {
    val result = characterData.delete(id)
    Ok(toJson(Map("result" -> result))).as("application/json")
  }
}
