package models.query

/**
 * Created with IntelliJ IDEA.
 * User: Adam
 * Date: 11/28/13
 * Time: 10:14 AM
 * To change this template use File | Settings | File Templates.
 */
object NPCharacterQuery {

  lazy final val selectAll =
    """
      |SELECT * FROM npcharacter;
    """.stripMargin

  lazy final val selectSpells =
    """
      |SELECT * FROM npcharacter_spell
    """.stripMargin

  lazy final val selectById =
    """
      |SELECT * FROM npcharacter WHERE id = {id}
    """.stripMargin

  lazy final val selectSpellsById =
    """
      |SELECT * FROM npcharacter_spell WHERE char_id = {id}
    """.stripMargin

  lazy final val insertCharacter =
    """
      |INSERT INTO npcharacter (name, class_id, model, health, race, level, hostility, faction, agro_radius) VALUES
      |({name}, {class_id}, {model}, {health}, {race}, {level}, {hostility}, {faction}, {agro_radius})
    """.stripMargin

  lazy final val insertSpell =
    """
      |INSERT INTO npcharacter_spell (spell_id, char_id) VALUES
      |({spell_id}, {char_id})
    """.stripMargin

  lazy final val updateCharacter =
    """
      |UPDATE npcharacter SET name = {name}, class_id = {class_id}, model = {model},
      |health = {health}, race = {race}, level = {level}, hostility = {hostility},
      |faction = {faction}, agro_radius = {agro_radius} WHERE id = {id}
    """.stripMargin

  lazy final val updateSpell =
    """
      |UPDATE npcharacter_spell SET spell_id = {spell_id} WHERE id = {id}
    """.stripMargin

  lazy final val deleteCharacter =
    """
      |DELETE FROM npcharacter WHERE id = {id}
    """.stripMargin

  lazy final val deleteSpells =
    """
      |DELETE FROM npcharacter_spell WHERE char_id = {id}
    """.stripMargin
}
