package com.aeskreis.dungeonraider.models.query

/**
 * Created by Adam on 2/10/14.
 */
object PCharacterQuery {
  lazy final val selectAllClasses =
    """
      |SELECT * FROM class
    """.stripMargin

  lazy final val selectAllForUser =
    """
      |SELECT * FROM pcharacter WHERE user_id = {user_id} ORDER BY id
    """.stripMargin

  lazy final val selectById =
    """
      |SELECT * FROM pcharacter WHERE id = {id}
    """.stripMargin

  lazy final val insertCharacter =
    """
      |INSERT INTO pcharacter (user_id, name, class_id, model, health, race, level, experience) VALUES
      |({user_id}, {name}, {class_id}, {model}, {health}, {race}, {level}, {experience})
    """.stripMargin

  lazy final val updateCharacter =
    """
      |UPDATE pcharacter SET user_id = {user_id}, name = {name}, class_id = {class_id}, health = {health},
      |race = {race}, level = {level}, experience = {experience} WHERE id = {id}
    """.stripMargin

  lazy final val deleteCharacter =
  """
    |DELETE FROM pcharacter WHERE id = {id}
  """.stripMargin

}
