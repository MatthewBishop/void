package world.gregs.voidps.engine.entity.character.mode.move

import org.rsmod.game.pathfinder.Route
import world.gregs.voidps.engine.entity.character.Character
import world.gregs.voidps.type.CoordGrid
import java.util.*

class Steps(
    internal val character: Character,
    private val steps: LinkedList<Step> = LinkedList<Step>()
) : List<Step> by steps {
    var destination: CoordGrid = CoordGrid.EMPTY
        private set

    fun peek(): Step? = steps.peek()

    fun poll(): Step = steps.poll()

    fun queueRoute(route: Route, target: CoordGrid? = null, noCollision: Boolean = false, noRun: Boolean = false) {
        queueSteps(route.waypoints.map { character.tile.copy(it.x, it.z) }, noCollision, noRun)
        destination = (target ?: steps.lastOrNull() ?: character.tile).step(noCollision, noRun)
    }

    fun queueStep(tile: CoordGrid, noCollision: Boolean = false, noRun: Boolean = false) {
        clear()
        steps.add(tile.step(noCollision, noRun))
        destination = tile.step(noCollision, noRun)
    }

    fun queueSteps(tiles: List<CoordGrid>, noCollision: Boolean = false, noRun: Boolean = false) {
        clear()
        steps.addAll(tiles.map { it.step(noCollision, noRun) })
        destination = steps.lastOrNull() ?: character.tile.step(noCollision, noRun)
    }

    /**
     * Updates all steps to have [noCollision] or [noRun]
     * Used for modifying existing paths, for creating new paths e.g.
     * to walk through doors use [queueSteps]
     */
    fun update(noCollision: Boolean = false, noRun: Boolean = false) {
        for (i in steps.indices) {
            steps[i] = steps[i].step(noCollision, noRun)
        }
        destination = destination.step(noCollision, noRun)
    }

    fun clearDestination() {
        destination = CoordGrid.EMPTY
    }

    fun clear() {
        steps.clear()
        clearDestination()
    }
}