package rs.dusk.world.entity.npc

import rs.dusk.engine.event.EventCompanion
import rs.dusk.engine.model.entity.index.npc.NPC
import rs.dusk.engine.model.entity.index.npc.NPCEvent

data class NPCDespawn(override val npc: NPC) : NPCEvent() {
    companion object : EventCompanion<NPCDespawn>
}