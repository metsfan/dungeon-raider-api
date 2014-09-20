package com.aeskreis.dungeonraider.lib

import com.rabbitmq.client.ConnectionFactory
import play.api.GlobalSettings
import play.api._
import play.api.mvc._
import play.api.mvc.Results._
import play.api.Play.current
import play.api.http.HeaderNames._
import play.api.libs.concurrent.Execution.Implicits._
import scala.concurrent.Future
import java.util.UUID

/**
 * Created by Adam on 5/27/14.
 */

object ServerCommands extends Enumeration {
  val Connect = Value(0)
  val Message = Value(1)
  val Disconnect = Value(2)
  val JoinChannel = Value(3)
  val FriendRequest = Value(4)
  val FriendResponse = Value(5)
  val StatusChange = Value(6)
}

object Global extends GlobalSettings {

  val UUIDZero = new UUID(0, 0)

  val mqFactory = new ConnectionFactory
  mqFactory.setHost("localhost")

  val connection = mqFactory.newConnection()
  val consumerChannel = connection.createChannel()

  override def doFilter(action: EssentialAction): EssentialAction = EssentialAction { request =>
    action.apply(request).map(_.withHeaders(
      ACCESS_CONTROL_ALLOW_ORIGIN -> "*",
      ACCESS_CONTROL_ALLOW_HEADERS -> "accept, origin, Content-type, x-json, x-prototype-version, x-requested-with",
      ACCESS_CONTROL_ALLOW_METHODS -> "POST, GET, PUT, DELETE, OPTIONS"
    ))
  }

  override def onBadRequest(request: RequestHeader, error: String) = {
    Future.successful(BadRequest("Bad Request: " + error))
  }

  override def onStart(app: Application): Unit = {
    super.onStart(app)
  }
}
