package world.gregs.voidps.engine.entity.character.mode.move

import world.gregs.voidps.engine.client.ui.chat.toInt
import world.gregs.voidps.type.CoordGrid

typealias Step = CoordGrid

private fun id(tile: Int, noCollision: Boolean = false, noRun: Boolean = false) =
    tile + (noCollision.toInt() shl 30) + (noRun.toInt() shl 31)

private fun noCollision(id: Int) = id shr 30 and 0x1 == 1
private fun noRun(id: Int) = id shr 31 and 0x1 == 1

fun CoordGrid.step(noCollision: Boolean, noRun: Boolean): Step {
    return CoordGrid(id(id and 0x3FFFFFFF, noCollision, noRun))
}

val Step.noCollision: Boolean
    get() = noCollision(id)

val Step.noRun: Boolean
    get() = noRun(id)