package world.gregs.voidps.world.activity.quest

import world.gregs.voidps.engine.client.Minimap
import world.gregs.voidps.engine.client.clearMinimap
import world.gregs.voidps.engine.client.minimap
import world.gregs.voidps.engine.client.ui.close
import world.gregs.voidps.engine.client.ui.open
import world.gregs.voidps.engine.entity.character.CharacterContext
import world.gregs.voidps.engine.get
import world.gregs.voidps.engine.map.instance.Instances
import world.gregs.voidps.engine.map.zone.DynamicZones
import world.gregs.voidps.type.MapSquareKey

private val tabs = listOf(
    "combat_styles",
    "task_system",
    "stats",
    "quest_journals",
    "inventory",
    "worn_equipment",
    "prayer_list",
    "modern_spellbook",
    "emotes",
    "notes"
)

fun CharacterContext.startCutscene(region: MapSquareKey): MapSquareKey {
    val instance = Instances.small()
    get<DynamicZones>().copy(region, instance)
    hideTabs()
    return instance
}

fun CharacterContext.hideTabs() {
    tabs.forEach {
        player.close(it)
    }
    player.minimap(Minimap.HideMap)
}

fun CharacterContext.stopCutscene(instance: MapSquareKey) {
    Instances.free(instance)
    get<DynamicZones>().clear(instance)
    player.open("fade_in")
    showTabs()
}

fun CharacterContext.showTabs() {
    tabs.forEach {
        player.open(it)
    }
    player.clearMinimap()
}