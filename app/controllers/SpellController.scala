package controllers

import play.api.mvc._
import models.data.SpellData
import models.parsers.SpellParser

object SpellController extends Controller with SpellParser {
  val spellData = new SpellData()

  def list = Action {
    Ok(jsonify(spellData.all(200)))
  }

  def get(id: String) = Action {
    Ok(jsonify(spellData.getById(id)))
  }

  def save(id: String) = Action {
    Ok("")
  }

  def delete(id: String) = Action {
    Ok("")
  }
}
