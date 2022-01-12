import world.gregs.voidps.engine.action.ActionFinished
import world.gregs.voidps.engine.action.ActionStarted
import world.gregs.voidps.engine.action.ActionType
import world.gregs.voidps.engine.action.action
import world.gregs.voidps.engine.entity.Direction
import world.gregs.voidps.engine.entity.Registered
import world.gregs.voidps.engine.entity.character.move.walkTo
import world.gregs.voidps.engine.entity.character.npc.NPC
import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.engine.entity.character.update.visual.forceChat
import world.gregs.voidps.engine.entity.character.update.visual.watch
import world.gregs.voidps.engine.event.EventHandler
import world.gregs.voidps.engine.event.on
import world.gregs.voidps.engine.utility.inject
import kotlin.random.Random

val npcs: NPCs by inject()

on<ActionFinished>({ it.id == "ducklings" }) { npc: NPC ->
    followParent(npc)
}

on<Registered>({ it.id == "ducklings" }) { npc: NPC ->
    followParent(npc)
}

fun followParent(npc: NPC) {
    npc.action(ActionType.Follow) {
        var parent: NPC? = null
        while (isActive && parent == null) {
            for (dir in Direction.cardinal) {
                parent = npcs[npc.tile.add(dir.delta)]?.firstOrNull { it != null && it.id.startsWith("duck") && it.id.endsWith("swim") } ?: continue
                break
            }
            val random = npc.tile.toCuboid(3).random()
            npc.walkTo(random, cancelAction = false)
            delay(Random.nextInt(0, 20))
        }

        if (parent != null) {
            var handler: EventHandler? = null
            try {
                handler = parent.events.on<NPC, ActionStarted>({ type == ActionType.Dying }) {
                    npc.forceChat = "Eek!"
                    cancel()
                }
                npc.watch(parent)
                while (isActive) {
                    if (!parent.followTarget.reached(npc.tile, npc.size)) {
                        npc.movement.set(parent.followTarget)
                    }
                    if (Random.nextInt(300) < 1) {
                        parent.forceChat = "Quack?"
                        delay(1)
                        npc.forceChat = if (Random.nextBoolean()) "Cheep Cheep!" else "Eep!"
                    }
                    delay()
                }
            } finally {
                npc.watch(null)
                handler?.let { parent.events.remove(it) }
            }
        }
    }
}