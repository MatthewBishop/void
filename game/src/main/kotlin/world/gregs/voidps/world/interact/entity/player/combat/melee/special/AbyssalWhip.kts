package world.gregs.voidps.world.interact.entity.player.combat.melee.special

import world.gregs.voidps.engine.entity.character.Character
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.update.visual.setAnimation
import world.gregs.voidps.engine.entity.character.update.visual.setGraphic
import world.gregs.voidps.engine.entity.item.Item
import world.gregs.voidps.engine.event.Priority
import world.gregs.voidps.engine.event.on
import world.gregs.voidps.network.encode.message
import world.gregs.voidps.world.interact.entity.combat.*
import world.gregs.voidps.world.interact.entity.player.combat.MAX_SPECIAL_ATTACK
import world.gregs.voidps.world.interact.entity.player.combat.drainSpecialEnergy
import world.gregs.voidps.world.interact.entity.player.combat.melee.specialAccuracyMultiplier
import world.gregs.voidps.world.interact.entity.player.combat.specialAttack
import world.gregs.voidps.world.interact.entity.player.energy.runEnergy

fun isWhip(item: Item?) = item != null && item.name.startsWith("abyssal_whip")

on<CombatSwing>({ !swung() && isWhip(it.weapon) }, Priority.LOW) { player: Player ->
    if (player.specialAttack && !drainSpecialEnergy(player, MAX_SPECIAL_ATTACK / 2)) {
        delay = -1
        return@on
    }
    player.setAnimation("whip_${player.attackType}")
    player.hit(target)
    delay = 4
}

on<CombatHit>({ isWhip(it.weapon) }) { player: Player ->
    player.setAnimation("whip_block")
}

// Special attack

specialAccuracyMultiplier(1.25, ::isWhip)

on<CombatHit>({ isWhip(weapon) && special }) { character: Character ->
    if (character is Player) {
        val tenPercent = (character.runEnergy / 100) * 10
        if (tenPercent > 0) {
            character.runEnergy -= tenPercent
            if (source is Player) {
                source.runEnergy += tenPercent
            }
            character.message("You feel drained!")
        }
    }
    character.setGraphic("energy_drain", height = 100)
}