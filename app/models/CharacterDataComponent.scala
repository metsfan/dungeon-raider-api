package models

import models.parsers.CharacterParser

trait CharacterDataComponent extends BaseDataComponent[NPCharacter] {
}

case class NPCharacter(var id: Int, name: String, health: Int, race: Int, level: Int,
                       class_id: Int, model: String, hostility: Int,
                       faction: Int, agro_radius: Double, spells: List[NPCharacterSpell])

case class NPCharacterSpell(var id: Int, spell_id: Int, char_id: Int);


