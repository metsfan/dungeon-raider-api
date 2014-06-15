package lib.json

import models.SpellTrigger
import play.api.libs.json.{JsResult, JsValue}
import play.api.libs.json.Json._
import models.SpellTrigger

/**
 * Created by Adam on 6/8/14.
 */
class SpellTriggerParser extends JsonObjectParser[SpellTrigger] {
  implicit val spellTriggerWrites = writes[SpellTrigger]
  implicit val spellTriggerReads = reads[SpellTrigger]

  override def toObject(obj: Option[SpellTrigger], data: JsValue): JsResult[SpellTrigger] = {
    _toObject(obj.getOrElse(SpellTrigger()), data, spellTriggerWrites, spellTriggerReads)
  }

  override def toJsonArray(objs: List[SpellTrigger]): JsValue = {
    _toJsonArray(objs, "triggers", spellTriggerWrites)
  }

  override def toJsonObject(obj: SpellTrigger): JsValue = {
    _toJsonObject(obj, "trigger", spellTriggerWrites)
  }
}
