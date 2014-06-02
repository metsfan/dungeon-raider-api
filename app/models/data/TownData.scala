package models.data

import models.parsers.TownParser
import models.Town
import play.api.db.DB
import anorm._
import models.query.TownQuery
import play.api.Play.current

/**
 * Created by Adam on 6/1/14.
 */
class TownData extends BaseData with TownParser {
  def getById(id: String): Option[Town] = {
    DB.withConnection { implicit conn =>
      SQL(TownQuery.selectById).on("id" -> id).as(townRowParser *).headOption
    }
  }

  def getByUserId(user_id: String): List[Town] = {
    DB.withConnection { implicit conn =>
      SQL(TownQuery.selectByUserId).on("user_id" -> user_id).as(townUserRowParser *)
    }
  }
}
