package controllers

import models.data.TownData
import play.api.mvc.Action
import models.parsers.TownParser
import java.util.UUID

/**
 * Created by Adam on 6/1/14.
 */
object TownController extends BaseController with TownParser {
  val townData = new TownData

  def get(id: UUID) = Action {
    Ok(jsonify(townData.getById(id).get))
  }

  def getForUser(user_id: UUID) = Action {
    Ok(jsonify(townData.getByUserId(user_id)))
  }
}
