package models

/**
 * Created by Adam on 2/10/14.
 */
case class PCharacter(var id: Int, user_id: Int, name: String, class_id: Int, model: String, health: Int,
                      race: Int, level: Int, experience: Int, var spells: Seq[Spell] = Seq[Spell]())
