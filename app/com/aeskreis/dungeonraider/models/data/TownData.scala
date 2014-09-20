package com.aeskreis.dungeonraider.models.data

import models._
import models.query.TownQuery
import play.api.Play.current
import scala.slick.driver.PostgresDriver.simple._
import play.api.db.slick.DB
import java.util.UUID
import models.UserTown
import models.Town
import com.aeskreis.dungeonraider.lib.json.TownParser

/**
 * Created by Adam on 6/1/14.
 */
class TownData extends BaseData with TownParser {
  val towns = TableQuery[Towns]
  val userTowns = TableQuery[UserTowns]

  def getById(id: UUID): Option[Town] = {
    DB.withSession { implicit session =>
      towns.filter(_.id === id).firstOption
    }
  }

  def getByUserId(user_id: UUID): List[UserTown] = {
    DB.withSession { implicit session =>
      val result = for {
        (t, u) <- towns innerJoin userTowns on (_.id === _.town_id)
      } yield (t, u)

      result.filter(_._2.user_id === user_id).list map { case (t, u) =>
        UserTown(t.id, t.name, t.map, t.zone, u.rank, u.is_default, u.leader)
      }
    }
  }
}
