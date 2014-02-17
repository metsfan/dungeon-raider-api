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
                 shape: Int, effects: Seq[SpellEffect], triggers: Seq[SpellTrigger])

case class SpellEffect(var id: Int, var spell_id: Int, min_value: Int, max_value: Int,
                       stat_type: Int, effect_type: Int, school: Int, duration: Int)

case class SpellTrigger(var id: Int, var spell_id: Int, trigger_spell_id: Int,
                        chance: Double, trigger_type: Int)
