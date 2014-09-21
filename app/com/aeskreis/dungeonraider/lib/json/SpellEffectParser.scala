package com.aeskreis.dungeonraider.lib.json

import play.api.libs.json.Json._
import com.aeskreis.dungeonraider.models.SpellEffect
import play.api.libs.json.{JsResult, JsValue}

/**
 * Created by Adam on 6/8/14.
 */
class SpellEffectParser extends JsonObjectParser[SpellEffect] {
  implicit val spellEffectWrites = writes[SpellEffect]
  implicit val spellEffectReads = reads[SpellEffect]

  override def toObject(obj: Option[SpellEffect], data: JsValue): JsResult[SpellEffect] = {
    _toObject(obj.getOrElse(SpellEffect()), data, spellEffectWrites, spellEffectReads)
  }

  override def toJsonArray(objs: List[SpellEffect]): JsValue = {
    _toJsonArray(objs, "effects", spellEffectWrites)
  }

  override def toJsonObject(obj: SpellEffect): JsValue = {
    _toJsonObject(obj, "effect", spellEffectWrites)
  }
}
