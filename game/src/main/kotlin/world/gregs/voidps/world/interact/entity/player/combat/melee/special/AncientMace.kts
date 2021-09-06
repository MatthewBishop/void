package world.gregs.voidps.world.interact.entity.player.combat.melee.special

import world.gregs.voidps.engine.entity.character.Character
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.entity.character.update.visual.setAnimation
import world.gregs.voidps.engine.entity.character.update.visual.setGraphic
import world.gregs.voidps.engine.entity.item.Item
import world.gregs.voidps.engine.event.on
import world.gregs.voidps.world.interact.entity.combat.CombatHit
import world.gregs.voidps.world.interact.entity.combat.CombatSwing
import world.gregs.voidps.world.interact.entity.combat.hit
import world.gregs.voidps.world.interact.entity.combat.weapon
import world.gregs.voidps.world.interact.entity.player.combat.MAX_SPECIAL_ATTACK
import world.gregs.voidps.world.interact.entity.player.combat.drainSpecialEnergy
import world.gregs.voidps.world.interact.entity.player.combat.specialAttack

fun isAncientMace(item: Item?) = item != null && item.name == "ancient_mace"

on<CombatSwing>({ !swung() && it.specialAttack && isAncientMace(it.weapon) }) { player: Player ->
    if (!drainSpecialEnergy(player, MAX_SPECIAL_ATTACK)) {
        delay = -1
        return@on
    }
    player.setAnimation("favour_of_the_war_god")
    player.setGraphic("favour_of_the_war_god")
    player.hit(target)
    delay = 5
}

on<CombatHit>({ isAncientMace(weapon) && special }) { character: Character ->
    val drain = damage / 10
    if (drain > 0) {
        character.levels.drain(Skill.Prayer, drain)
        source.levels.restore(Skill.Prayer, drain)
    }
}