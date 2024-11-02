package world.gregs.voidps.engine.map.collision

import world.gregs.voidps.type.CoordGrid
import world.gregs.voidps.type.ZoneKey

class GameObjectCollisionRemove(
    private val collisions: Collisions
) : GameObjectCollision() {

    override fun modifyTile(x: Int, y: Int, level: Int, block: Int, direction: Int) {
        var flags = collisions.flags[ZoneKey.tileIndex(x, y, level)]
        if (flags == null) {
            flags = collisions.allocateIfAbsent(x, y, level)
        }
        flags[CoordGrid.index(x, y)] = flags[CoordGrid.index(x, y)] and CollisionFlags.inverse[direction or block]
    }
}