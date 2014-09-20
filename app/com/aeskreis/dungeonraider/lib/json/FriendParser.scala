package com.aeskreis.dungeonraider.lib.json

import models.Friend
import play.api.libs.json._
import play.api.libs.json.Json._

/**
 * Created by Adam on 8/30/14.
 */
class FriendParser extends JsonObjectParser[Friend] {
  implicit val friendReads = reads[Friend]
  implicit val friendWrites = writes[Friend]

  override def toObject(obj: Option[Friend], data: JsValue): JsResult[Friend] = {
    _toObject(obj.getOrElse(Friend()), data, friendWrites, friendReads)
  }

  override def toJsonArray(objs: List[Friend]): JsValue = {
    _toJsonArray(objs, "friends", friendWrites)
  }

  override def toJsonObject(obj: Friend): JsValue = {
    _toJsonObject(obj, "friend", friendWrites)
  }
}
