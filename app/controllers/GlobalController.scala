package controllers

import play.api.mvc.Action

/**
 * Created by Adam on 5/28/14.
 */
object GlobalController extends BaseController {
  def checkPreFlight(path: String) = Action {
    Ok("").withHeaders(
      ACCESS_CONTROL_ALLOW_ORIGIN -> "*",
      ACCESS_CONTROL_ALLOW_HEADERS -> "accept, origin, Content-type, x-json, x-prototype-version, x-requested-with",
      ACCESS_CONTROL_ALLOW_METHODS -> "POST"
    )
  }
}
