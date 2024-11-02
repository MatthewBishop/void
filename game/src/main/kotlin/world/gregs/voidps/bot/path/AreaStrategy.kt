package world.gregs.voidps.bot.path

import world.gregs.voidps.type.CoordGrid
import world.gregs.voidps.type.Area

class AreaStrategy(
    val area: Area
) : NodeTargetStrategy() {

    override fun reached(node: Any): Boolean {
        return node is CoordGrid && node in area
    }
}