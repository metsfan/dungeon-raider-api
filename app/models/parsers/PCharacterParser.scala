package models.parsers

import anorm._
import anorm.SqlParser._
import models.PCharacter
import play.api.libs.json.Json._
import play.api.libs.json._
import scala.collection.mutable

/**
 * Created by Adam on 2/10/14.
 */
trait PCharacterParser {
  implicit val characterWrites = writes[PCharacter]

  def charRowParser: RowParser[PCharacter] = {
    get[Int]("id") ~
    get[String]("name") ~
    get[Int]("class_id") ~
    get[String]("model") ~
    get[Int]("health") ~
    get[Int]("race") ~
    get[Int]("level") ~
    get[Int]("experience") ~
    get[Int]("user_id") map {
      case id ~ name ~ classId ~ model ~ health ~ race ~ level ~ experience ~ userId => {
        PCharacter(id, userId, name, classId, model, health, race, level, experience)
      }
    }
  }

  def charFormParser(data: JsValue, id: Int): PCharacter = {

    val id = (data \ "id").as[Int]
    val name = (data \ "name").as[String]
    val health = (data \ "health").as[Int]
    val race = (data \ "race").as[Int]
    val level = (data \ "level").as[Int]
    val class_id = (data \ "class_id").as[Int]
    val model = (data \ "model").as[String]
    val user_id = (data \ "user_id").as[Int]
    val experience = (data \ "experience").as[Int]

    PCharacter(id, user_id, name, class_id, model, health, race, level, experience)
  }

  def jsonify(characters: List[PCharacter]) = {
    toJson(characters)
  }

  def jsonify(character: Option[PCharacter]) = {
    toJson(character)
  }
}
