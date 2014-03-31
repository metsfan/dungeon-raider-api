package models

/**
 * Created by Adam on 2/8/14.
 */

case class NPCharacter(var id: Int, name: String, health: Int, race: Int, level: Int,
                       class_id: Int, model: String, hostility: Int,
                       faction: Int, agro_radius: Double, spells: List[Spell])

case class NPCharacterSpell(var id: Int, spell_id: Int, char_id: Int, data: Spell)
