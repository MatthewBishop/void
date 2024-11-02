package world.gregs.voidps.bot

import world.gregs.voidps.engine.entity.character.player.Player
import world.gregs.voidps.type.CoordGrid
import world.gregs.voidps.type.Area

data class Task(
    val name: String,
    val block: suspend Player.() -> Unit,
    val area: Area? = null,
    var spaces: Int = 1,
    val requirements: Collection<Player.() -> Boolean> = emptySet()
) {
    fun full() = spaces <= 0

    fun distanceTo(tile: CoordGrid): Int {
        return if (area == null) 0 else tile.distanceTo(area.random())
    }
}