package models.data

import play.api.Play.current
import models.query.PCharacterQuery
import models.{CharClasses, PCharacters, CharClass, PCharacter}
import scala.slick.driver.PostgresDriver.simple._
import play.api.db.slick.DB
import java.util.UUID
import lib.json.PCharacterParser

/**
 * Created by Adam on 2/10/14.
 */
class PCharacterData extends BaseData {
  val pcharacters = TableQuery[PCharacters]
  val classes = TableQuery[CharClasses]

  def all(user_id: UUID): List[PCharacter] = {
    DB.withSession { implicit session =>
      pcharacters.filter(_.user_id === user_id).list
    }
  }

  def get(id: UUID): Option[PCharacter] = {
    DB.withSession { implicit session =>
      pcharacters.filter(_.id === id).firstOption
    }
  }

  def allClasses: List[CharClass] = {
    DB.withSession { implicit session =>
      classes.list
    }
  }
}
