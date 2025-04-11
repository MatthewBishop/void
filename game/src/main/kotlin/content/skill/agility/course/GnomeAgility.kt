package content.skill.agility.course

import world.gregs.voidps.engine.entity.character.npc.NPCs
import world.gregs.voidps.type.ZoneKey
import world.gregs.voidps.type.random

internal fun NPCs.gnomeTrainer(message: String, zone: ZoneKey) {
    val trainer = get(zone).randomOrNull(random) ?: return
    trainer.say(message)
}

internal fun NPCs.gnomeTrainer(message: String, zones: List<ZoneKey>) {
    for (zone in zones) {
        val trainer = get(zone).randomOrNull(random) ?: continue
        trainer.say(message)
        break
    }
}