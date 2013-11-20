package models.data.query

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
}
