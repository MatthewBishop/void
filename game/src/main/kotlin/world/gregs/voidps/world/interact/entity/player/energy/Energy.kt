package world.gregs.voidps.world.interact.entity.player.energy

import world.gregs.voidps.engine.client.sendRunEnergy
import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.engine.entity.get
import world.gregs.voidps.engine.entity.set

const val MAX_ENERGY = 10000

fun Player.energyPercent() = (runEnergy / MAX_ENERGY.toDouble() * 100).toInt()

var Player.runEnergy: Int
    get() = this["energy", MAX_ENERGY]
    set(value) {
        this["energy", true] = value.coerceIn(0, MAX_ENERGY)
        sendRunEnergy(energyPercent())
    }