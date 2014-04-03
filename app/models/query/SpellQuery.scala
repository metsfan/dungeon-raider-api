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
      |SELECT s.*,c.name as class_name, c.id as class_id, cs.slot
      |FROM spell s
      |LEFT JOIN class c ON c.id=s.class_id
      |LEFT JOIN class_spell cs ON cs.class_id=c.id
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
      |SELECT s.*,c.name as class_name, c.id as class_id, cs.slot
      |FROM spell s
      |LEFT JOIN class c ON c.id=s.class_id
      |LEFT JOIN class_spell cs ON cs.class_id=c.id AND cs.spell_id=s.id
      |WHERE s.class_id={classId}
      |ORDER BY s.id
    """.stripMargin

  lazy final val selectById =
    """
      |SELECT s.*,c.name as class_name, c.id as class_id, cs.slot
      |FROM spell s
      |LEFT JOIN class c ON c.id=s.class_id
      |LEFT JOIN class_spell cs ON cs.class_id=c.id
      |WHERE s.id={spellId} LIMIT 1
    """.stripMargin

  lazy final val selectEffectsForSpell =
    """
      |SELECT * FROM spell_effect WHERE spell_id={spellId} ORDER BY delta
    """.stripMargin

  lazy final val selectTriggersForSpell =
    """
      |SELECT * FROM spell_trigger WHERE spell_id={spellId}
    """.stripMargin

  lazy final val insertSpell =
    """
      |INSERT INTO spell (name, cast_time, cooldown, spell_type, cast_type, spell_radius, spell_range, shape, class_id, self_cast, icon_url) VALUES
      |({name}, {cast_time}, {cooldown}, {spell_type}, {cast_type}, {radius}, {range}, {shape}, {class_id}, {self_cast}, {icon_url})
    """.stripMargin

  lazy final val insertSpellEffect =
    """
      |INSERT INTO spell_effect (spell_id, effect_type, damage_source, buff_source,
      |percent_source_min, percent_source_max, flat_amount_min, flat_amount_max,
      |dot_tick, dot_duration, buff_duration, mechanic, school, script_name,
      |script_arguments, delta, max_stacks)
      |VALUES
      |({spell_id}, {effect_type}, {damage_source}, {buff_source},
      |{percent_source_min}, {percent_source_max}, {flat_amount_min}, {flat_amount_max},
      |{dot_tick}, {dot_duration}, {buff_duration}, {mechanic}, {school},
      |{script_name}, {script_arguments}, {delta}, {max_stacks})
    """.stripMargin

  lazy final val insertSpellTrigger =
    """
      |INSERT INTO spell_trigger (spell_id, trigger_spell_id, chance, trigger_type) VALUES
      |({spell_id}, {trigger_spell_id}, {chance}, {trigger_type})
    """.stripMargin

  lazy final val insertSpellClass =
    """
      |INSERT INTO class_spell (class_id, spell_id) VALUES ({class_id}, {spell_id});
    """.stripMargin

  lazy final val updateSpell =
    """
      |UPDATE spell SET name = {name}, cast_time = {cast_time}, cooldown = {cooldown}, spell_type = {spell_type},
      |cast_type = {cast_type}, spell_radius = {radius}, spell_range = {range},
      |shape = {shape}, class_id = {class_id}, self_cast = {self_cast}, icon_url = {icon_url} WHERE id = {spell_id}
    """.stripMargin

  lazy final val updateSpellEffect =
    """
      |UPDATE spell_effect SET spell_id = {spell_id}, effect_type = {effect_type},
      |damage_source = {damage_source}, buff_source = {buff_source},
      |percent_source_min = {percent_source_min}, percent_source_max = {percent_source_max},
      |flat_amount_min = {flat_amount_min}, flat_amount_max = {flat_amount_max},
      |dot_tick = {dot_tick}, dot_duration = {dot_duration}, buff_duration = {buff_duration},
      |mechanic = {mechanic}, school = {school}, script_name = {script_name},
      |script_arguments = {script_arguments}, delta = {delta}, max_stacks = {max_stacks}
      |WHERE id = {effect_id}
    """.stripMargin

  lazy final val updateSpellTrigger =
    """
      |UPDATE spell_trigger SET spell_id = {spell_id}, trigger_spell_id = {trigger_spell_id},
      |chance = {chance}, trigger_type = {trigger_type} WHERE id = {trigger_id}
    """.stripMargin

  lazy final val updateSpellClass =
    """
      |UPDATE class_spell SET icon_url = {icon_url} WHERE class_id = {class_id} AND {spell_id} = {spell_id}
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

  lazy final val deleteEffect =
    """
      |DELETE FROM spell_effect WHERE id = {id}
    """.stripMargin

  lazy final val deleteTrigger =
    """
      |DELETE FROM spell_trigger WHERE id = {id}
    """.stripMargin

  lazy final val updateSpellSlot =
    """
      |UPDATE class_spell SET slot = {slot}
      |WHERE spell_id = {spell_id} AND class_id = {class_id}
    """.stripMargin

  lazy final val selectSpellByCharacter =
    """
      |SELECT s.*,c.name as class_name, c.id as class_id, cs.slot
      |FROM spell s
      |INNER JOIN npcharacter_spell nps ON nps.spell_id = s.id
      |LEFT JOIN class c ON c.id=s.class_id
      |LEFT JOIN class_spell cs ON cs.class_id=c.id
      |WHERE nps.char_id = {char_id}
    """.stripMargin

  lazy final val selectSpellEffectsByCharacter =
    """
      |SELECT s.*
      |FROM spell_effect s
      |INNER JOIN npcharacter_spell nps ON nps.spell_id = s.id
      |WHERE nps.char_id = {char_id}
    """.stripMargin

  lazy final val selectSpellTriggersByCharacter =
    """
      |SELECT s.*
      |FROM spell_trigger s
      |INNER JOIN npcharacter_spell nps ON nps.spell_id = s.id
      |WHERE nps.char_id = {char_id}
    """.stripMargin
}
