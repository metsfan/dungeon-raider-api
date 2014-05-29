package lib

import play.api.GlobalSettings
import play.api._
import play.api.mvc._
import play.api.Play.current
import play.api.http.HeaderNames._
import play.api.libs.concurrent.Execution.Implicits._

/**
 * Created by Adam on 5/27/14.
 */
object Global extends GlobalSettings {

  override def doFilter(action: EssentialAction): EssentialAction = EssentialAction { request =>
    action.apply(request).map(_.withHeaders(
      ACCESS_CONTROL_ALLOW_ORIGIN -> "*",
      ACCESS_CONTROL_ALLOW_HEADERS -> "accept, origin, Content-type, x-json, x-prototype-version, x-requested-with",
      ACCESS_CONTROL_ALLOW_METHODS -> "POST"
    ))
  }
}
