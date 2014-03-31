package models.data

import models.parsers.PCharacterParser
import play.api.db.DB
import play.api.Play.current
import anorm._
import models.query.PCharacterQuery
import models.{CharClass, PCharacter}

/**
 * Created by Adam on 2/10/14.
 */
class PCharacterData extends BaseData with PCharacterParser {

  def all(user_id: String): List[PCharacter] = {
    DB.withConnection { implicit conn =>
      SQL(PCharacterQuery.selectAllForUser).on("user_id" -> user_id.toInt).as(charRowParser *)
    }
  }

  def get(id: String): Option[PCharacter] = {
    DB.withConnection { implicit conn =>
      SQL(PCharacterQuery.selectById).on("id" -> id.toInt).as(charRowParser *).headOption
    }
  }

  def allClasses: List[CharClass] = {
    DB.withConnection { implicit conn =>
      SQL(PCharacterQuery.selectAllClasses).as(classRowParser *)
    }
  }
}
