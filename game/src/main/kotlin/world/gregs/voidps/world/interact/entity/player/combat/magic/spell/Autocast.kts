package world.gregs.voidps.world.interact.entity.player.combat.magic.spell

import world.gregs.voidps.engine.client.ui.interfaceOption
import world.gregs.voidps.engine.client.variable.variableClear
import world.gregs.voidps.engine.data.definition.InterfaceDefinitions
import world.gregs.voidps.engine.inject
import world.gregs.voidps.engine.inv.itemChange
import world.gregs.voidps.network.visual.update.player.EquipSlot
import world.gregs.voidps.world.interact.entity.combat.attackRange

val interfaceDefinitions: InterfaceDefinitions by inject()

interfaceOption("Autocast", id = "*_spellbook") {
    val value: Int? = interfaceDefinitions.getComponent(id, component)?.getOrNull("cast_id")
    if (value == null || player["autocast", 0] == value) {
        player.clear("autocast")
    } else {
        player["autocast_spell"] = component
        player.attackRange = 8
        player["autocast"] = value
    }
}

variableClear("autocast") { player ->
    player.clear("autocast_spell")
}

itemChange(EquipSlot.Weapon, "worn_equipment") { player ->
    player.clear("autocast")
}