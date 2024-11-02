package world.gregs.voidps.tools.graph

import world.gregs.voidps.type.Direction
import world.gregs.voidps.type.CoordGrid
import world.gregs.voidps.engine.map.collision.Collisions

interface TileTraversalStrategy {

    fun blocked(collisions: Collisions, x: Int, y: Int, level: Int, size: Int, direction: Direction): Boolean = true

    fun blocked(collisions: Collisions, tile: CoordGrid, size: Int, direction: Direction): Boolean = blocked(collisions, tile.x, tile.y, tile.level, size, direction)

}