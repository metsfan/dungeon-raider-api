package models

import play.api.db.DB
import anorm._
import models.parsers.SpellParser

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/19/13
 * Time: 12:28 AM
 * To change this template use File | Settings | File Templates.
 */
trait SpellDataComponent extends BaseDataComponent[Spell] {
}

case class Spell(var id: Int, name: String, cast_time: Int, cooldown: Int,
                 spell_type: Int, cast_type: Int, radius: Double, range: Double,
                 shape: Int, effects: List[SpellEffect], triggers: List[SpellTrigger]) extends Serializable

case class SpellEffect(var id: Int, var spell_id: Int, min_value: Int, max_value: Int,
                       stat_type: Int, effect_type: Int, school: Int, duration: Int) extends Serializable

case class SpellTrigger(var id: Int, var spell_id: Int, trigger_spell_id: Int,
                        chance: Double, trigger_type: Int) extends Serializable
