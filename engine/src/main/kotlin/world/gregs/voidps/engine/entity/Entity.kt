package world.gregs.voidps.engine.entity

import world.gregs.voidps.engine.entity.character.Character
import world.gregs.voidps.engine.entity.character.size
import world.gregs.voidps.engine.entity.obj.GameObject
import world.gregs.voidps.type.CoordGrid

/**
 * An identifiable object with a physical spatial location
 */
interface Entity {
    var tile: CoordGrid
}

fun CoordGrid.distanceTo(entity: Entity) = when (entity) {
    is Character -> distanceTo(entity.tile, entity.size, entity.size)
    is GameObject -> distanceTo(entity.tile, entity.width, entity.height)
    else -> distanceTo(entity.tile)
}