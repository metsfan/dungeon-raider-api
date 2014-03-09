package models

import play.api.db.DB
import play.api.Play.current
import scala.collection.mutable
import java.sql._
import anorm._
import models.SpellTrigger
import models.SpellEffect
import models.query.SpellQuery
import anorm.ParameterValue
import models.SpellTrigger
import models.SpellEffect
import models.parsers.SpellParser

/**
 * Created by Adam on 2/8/14.
 */

case class Spell(var id: Int, name: String, cast_time: Int, cooldown: Int,
                 spell_type: Int, cast_type: Int, radius: Double, range: Double,
                 shape: Int, self_cast: Boolean, char_class: Option[CharClass],
                 slot: Option[String], icon_url: Option[String],
                 effects: Seq[SpellEffect], triggers: Seq[SpellTrigger])

case class SpellEffect(var id: Int, var spell_id: Int, effect_type: Int,
                        damage_source: Int, buff_source: Int, percent_source_min: Int,
                        percent_source_max: Int, flat_amount_min: Int, flat_amount_max: Int,
                        dot_tick: Int, dot_duration: Int, buff_duration: Int, mechanic: Int,
                        school: Int,  script_name: Option[String], script_arguments: Option[String])

case class SpellTrigger(var id: Int, var spell_id: Int, trigger_spell_id: Int,
                        chance: Double, trigger_type: Int)

case class ClassSpell(var id: Int, spell_id: Int, class_id: Int,
                       slot: Int)
