package lib.json

import models._
import play.api.libs.json.Json._
import play.api.libs.json._
import java.util.UUID

/**
 * Created by Adam on 6/7/14.
 */
abstract class JsonObjectParser[T]
{

  protected def _toJsonObject(obj: T, element: String, jsonWrites: Writes[T]): JsValue = {
    toJson(Map(element -> (jsonWrites.writes(obj).asInstanceOf[JsObject] ++ getExtra(obj))))
  }

  protected def _toJsonObject(obj: T, jsonWrites: Writes[T]): JsValue = {
    (jsonWrites.writes(obj).asInstanceOf[JsObject] ++ getExtra(obj))
  }

  protected def _toJsonArray(objs: List[T], element: String, jsonWrites: Writes[T]): JsValue = {
    toJson(Map(element -> objs.map(_toJsonObject(_, jsonWrites))))
  }

  protected def _toObject(obj: T, data: JsValue, jsonWrites: Writes[T], jsonReads: Reads[T]): JsResult[T] = {
    val jsObject = jsonWrites.writes(obj).asInstanceOf[JsObject] ++ data.asInstanceOf[JsObject]
    jsonReads.reads(jsObject)
  }

  protected def getExtra(obj: T): JsObject = {
    JsObject(Seq[(String, JsValue)]())
  }

  def toJsonObject(obj: T): JsValue
  def toJsonArray(objs: List[T]): JsValue
  def toObject(obj: Option[T], data: JsValue): JsResult[T]
}
