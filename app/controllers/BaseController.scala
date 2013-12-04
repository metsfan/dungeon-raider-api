package controllers

import play.api.mvc._
import play.api.libs.json._

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/30/13
 * Time: 9:04 AM
 * To change this template use File | Settings | File Templates.
 */
trait BaseController extends Controller {

  def parseRequestJSON(request: Request[AnyContent]): JsValue = {
    if(request.body.asText.isDefined) {
      Json.parse(request.body.asText.get)
    } else if(request.body.asJson.isDefined) {
      request.body.asJson.get
    } else {
      null
    }
  }
}
