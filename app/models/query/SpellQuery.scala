package models.query

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/19/13
 * Time: 10:09 PM
 * To change this template use File | Settings | File Templates.
 */
object SpellQuery {

  lazy final val selectAll =
    """
      |SELECT * FROM spell
    """.stripMargin

  lazy final val selectAllEffects =
    """
      |SELECT * FROM spell_effect
    """.stripMargin

  lazy final val selectAllTriggers =
    """
      |SELECT * FROM spell_trigger
    """.stripMargin

  lazy final val selectAllForClass =
    """
      |SELECT s.*
      |FROM spell s
      |INNER JOIN class_spell cs ON cs.spell_id=s.id
      |WHERE cs.class_id={classId}
      |ORDER BY s.id
    """.stripMargin

  lazy final val selectById =
    """
      |SELECT * FROM spell WHERE id={spellId} LIMIT 1
    """.stripMargin

  lazy final val selectEffectsForSpell =
    """
      |SELECT * FROM spell_effect WHERE spell_id={spellId}
    """.stripMargin

  lazy final val selectTriggersForSpell =
    """
      |SELECT * FROM spell_trigger WHERE spell_id={spellId}
    """.stripMargin

  lazy final val insertSpell =
    """
      |INSERT INTO spell (name, cast_time, cooldown, spell_type, cast_type, spell_radius, spell_range, shape) VALUES
      |({name}, {cast_time}, {cooldown}, {spell_type}, {cast_type}, {radius}, {range}, {shape})
    """.stripMargin

  lazy final val insertSpellEffect =
    """
      |INSERT INTO spell_effect (spell_id, min_value, max_value, stat_type, effect_type, school) VALUES
      |({spell_id}, {min_value}, {max_value}, {stat_type}, {effect_type}, {school})
    """.stripMargin

  lazy final val insertSpellTrigger =
    """
      |INSERT INTO spell_trigger (spell_id, trigger_spell_id, chance, trigger_type) VALUES
      |({spell_id}, {trigger_spell_id}, {chance}, {trigger_type})
    """.stripMargin

  lazy final val updateSpell =
    """
      |UPDATE spell SET name = {name}, cast_time = {cast_time}, cooldown = {cooldown}, spell_type = {spell_type},
      |cast_type = {cast_type}, spell_radius = {radius}, spell_range = {range}, shape = {shape} WHERE id = {spell_id}
    """.stripMargin

  lazy final val updateSpellEffect =
    """
      |UPDATE spell_effect SET spell_id = {spell_id}, min_value = {min_value}, max_value = {max_value},
      |stat_type = {stat_type}, effect_type = {effect_type}, school = {school} WHERE id = {effect_id}
    """.stripMargin

  lazy final val updateSpellTrigger =
    """
      |UPDATE spell_trigger SET spell_id = {spell_id}, trigger_spell_id = {trigger_spell_id},
      |chance = {chance}, trigger_type = {trigger_type} WHERE id = {trigger_id}
    """.stripMargin

  lazy final val deleteSpell =
    """
      |DELETE FROM spell WHERE id = {id}
    """.stripMargin

  lazy final val deleteEffects =
    """
      |DELETE FROM spell_effect WHERE spell_id = {spell_id}
    """.stripMargin

  lazy final val deleteTriggers =
    """
      |DELETE FROM spell_trigger WHERE spell_id = {spell_id}
    """.stripMargin
}