package lib.json

import models._
import play.api.libs.json.Json._
import play.api.libs.json._
import models.CharClass
import models.PCharacter
import models.SpellTrigger
import models.Spell

/**
 * Created by Adam on 2/10/14.
 */
class PCharacterParser extends JsonObjectParser[PCharacter] {
  implicit val charClassWrites = writes[CharClass]

  implicit val characterWrites = writes[PCharacter]
  implicit val characterReads = reads[PCharacter]

  val spellParser = new SpellParser

  def jsonifyClasses(classes: List[CharClass]): JsValue = {
    toJson(classes)
  }

  override def toObject(obj: Option[PCharacter], data: JsValue): JsResult[PCharacter] = {
    _toObject(obj.getOrElse(PCharacter()), data, characterWrites, characterReads)
  }

  override def toJsonArray(objs: List[PCharacter]): JsValue = {
    _toJsonArray(objs, "characters", characterWrites)
  }

  override def toJsonObject(obj: PCharacter): JsValue = {
    _toJsonObject(obj, "character", characterWrites)
  }

  override def getExtra(obj: PCharacter): JsObject = {
    spellParser.toJsonArray(obj.spells).asInstanceOf[JsObject]
  }
}
