package world.gregs.voidps.world.interact.entity.player.combat.melee.special

import world.gregs.voidps.engine.entity.Direction
import world.gregs.voidps.engine.entity.character.Character
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.character.player.Players
import world.gregs.voidps.engine.entity.character.update.visual.setAnimation
import world.gregs.voidps.engine.entity.character.update.visual.setGraphic
import world.gregs.voidps.engine.entity.item.Item
import world.gregs.voidps.engine.entity.list.PooledMapList
import world.gregs.voidps.engine.event.on
import world.gregs.voidps.engine.utility.inject
import world.gregs.voidps.world.interact.entity.combat.*
import world.gregs.voidps.world.interact.entity.player.combat.drainSpecialEnergy
import world.gregs.voidps.world.interact.entity.player.combat.specialAttack

fun isDragon2hSword(item: Item?) = item != null && item.name.startsWith("dragon_2h_sword")

val players: Players by inject()
val npcs: NPCs by inject()

on<CombatSwing>({ !swung() && it.specialAttack && isDragon2hSword(it.weapon) }) { player: Player ->
    if (!drainSpecialEnergy(player, 600)) {
        delay = -1
        return@on
    }
    player.setAnimation("powerstab")
    player.setGraphic("powerstab")
    if (player.inMultiCombat) {
        val list = mutableListOf<Character>()
        list.add(target)
        val characters: PooledMapList<out Character> = if (target is Player) players else npcs
        Direction.values.reversed().forEach { dir ->
            val tile = player.tile.add(dir)
            list.addAll(characters[tile]?.filterNotNull() ?: return@forEach)
        }
        list
            .filter { it.inMultiCombat && canAttack(player, it) }
            .take(if (target is Player) 3 else 15)
            .onEach {
                player.hit(it)
            }
    } else {
        player.hit(target)
    }
    delay = 7
}