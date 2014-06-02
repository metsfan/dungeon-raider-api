package models.parsers

import play.api.libs.json.Json._
import models._
import anorm.{~, RowParser}
import anorm.SqlParser._
import anorm.~
import models.Town
import play.api.libs.json.JsValue

/**
 * Created by Adam on 6/1/14.
 */
trait TownParser {
  implicit val townWrites = writes[Town]

  def townRowParser(): RowParser[Town] = {
    get[String]("id") ~
    get[String]("name") ~
    get[String]("map") ~
    get[Int]("zone") map {
    case id ~ name ~ map ~ zone =>
      Town(id, name, map, zone)
    }
  }

  def townUserRowParser(): RowParser[Town] = {
    get[String]("id") ~
    get[String]("name") ~
    get[String]("map") ~
    get[Int]("zone") ~
    get[Int]("rank") ~
    get[Boolean]("is_default") map {
    case id ~ name ~ map ~ zone ~ rank ~ isDefault =>
      Town(id, name, map, zone, rank, isDefault)
    }
  }

  def jsonify(towns: List[Town]): JsValue = {
    toJson(Map("towns" -> toJson(towns)))
  }

  def jsonify(town: Town): JsValue = {
    toJson(Map("town" -> toJson(town)))
  }
}
