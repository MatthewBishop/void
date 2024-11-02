package world.gregs.voidps.tools.graph

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test
import world.gregs.voidps.engine.entity.obj.GameObject
import world.gregs.voidps.engine.map.collision.Collisions
import world.gregs.voidps.type.Direction
import world.gregs.voidps.type.CoordGrid

@Disabled
internal class MapGraphTest {

    private lateinit var strategy: TileTraversalStrategy
    private lateinit var collisions: Collisions
    private lateinit var graph: MapGraph

    @BeforeEach
    fun setup() {
        strategy = mockk(relaxed = true)
        collisions = mockk(relaxed = true)
        graph = MapGraph(mockk(), mockk(), mockk(), collisions)
    }

    @Test
    fun `Flood fill empty tile`() {
        val tile = CoordGrid(0, 0)
        val tiles = graph.getFloodedTiles(strategy, tile, tile.toCuboid(width = 1, height = 1))
        assertEquals(mapOf(tile to 0), tiles)
    }

    @Test
    fun `Flood fill 2x2`() {
        val tile = CoordGrid(0, 0)
        val tiles = graph.getFloodedTiles(strategy, tile, tile.toCuboid(width = 2, height = 2))
        assertEquals(mapOf(tile to 0, CoordGrid(0, 1) to 1, CoordGrid(1, 0) to 1, CoordGrid(1, 1) to 1), tiles)
    }

    @Test
    fun `Flood fill 2x2 offset start`() {
        val tile = CoordGrid(1, 1)
        val tiles = graph.getFloodedTiles(strategy, tile, CoordGrid(0).toCuboid(width = 2, height = 2))
        assertEquals(mapOf(tile to 0, CoordGrid(1, 0) to 1, CoordGrid(0, 1) to 1, CoordGrid(0, 0) to 1), tiles)
    }

    @Test
    fun `Flood fill 2x2 ignore collision`() {
        every { strategy.blocked(collisions, CoordGrid(0, 0), 1, Direction.NORTH_EAST) } returns true
        every { strategy.blocked(collisions, CoordGrid(0, 1), 1, Direction.EAST) } returns true
        every { strategy.blocked(collisions, CoordGrid(1, 0), 1, Direction.NORTH) } returns true
        val tile = CoordGrid(0, 0)
        val tiles = graph.getFloodedTiles(strategy, tile, tile.toCuboid(width = 2, height = 2))
        assertEquals(mapOf(tile to 0, CoordGrid(0, 1) to 1, CoordGrid(1, 0) to 1), tiles)
    }

    @Test
    fun `Other levels collision ignored`() {
        every { strategy.blocked(collisions, CoordGrid(0, 0, 1), 1, any()) } returns true
        val tile = CoordGrid(0, 0)
        val tiles = graph.getFloodedTiles(strategy, tile, tile.toCuboid(width = 1, height = 1))
        assertEquals(mapOf(tile to 0), tiles)
    }

    @Test
    fun `Euclidean distance`() {
        assertEquals(5.0, graph.euclidean(CoordGrid(3, 4), CoordGrid(7, 1)))
    }

    @Test
    fun `Find centroid of a line`() {
        assertEquals(CoordGrid(0, 1), graph.centroid(setOf(CoordGrid(0, 0), CoordGrid(0, 2))))
    }

    @Test
    fun `Find centroid of a diagonal line`() {
        assertEquals(CoordGrid(1, 1), graph.centroid(setOf(CoordGrid(0, 0), CoordGrid(2, 2))))
    }

    @Test
    fun `Find centroid of a square`() {
        assertEquals(CoordGrid(1, 1), graph.centroid(setOf(CoordGrid(0, 0), CoordGrid(0, 2), CoordGrid(2, 2), CoordGrid(2, 0))))
    }

    @Test
    fun `Find centroid of a rectangle`() {
        assertEquals(CoordGrid(1, 1), graph.centroid(setOf(CoordGrid(0, 0), CoordGrid(0, 2), CoordGrid(3, 2), CoordGrid(3, 0))))
    }

    @Test
    fun `Find filled points`() {
        assertEquals(listOf(CoordGrid(0, 0)), graph.getCenterPoints(strategy, CoordGrid(0).toCuboid(width = 2, height = 2)))
    }

    @Test
    fun `No free center points`() {
        every { strategy.blocked(collisions, any(), 1, any()) } returns true
        assertEquals(listOf<CoordGrid>(), graph.getCenterPoints(strategy, CoordGrid(0).toCuboid(width = 3, height = 3)))
    }

    @Test
    fun `Find not collided points`() {
        every { strategy.blocked(collisions, CoordGrid(0, 0), 1, Direction.NONE) } returns true
        every { strategy.blocked(collisions, CoordGrid(1, 0), 1, Direction.WEST) } returns true
        assertEquals(listOf(CoordGrid(1, 0)), graph.getCenterPoints(strategy, CoordGrid(0).toCuboid(width = 3, height = 2)))
    }

    @Test
    fun `Find two separated filled points`() {
        every { strategy.blocked(collisions, CoordGrid(0, 1), 1, Direction.NONE) } returns true
        every { strategy.blocked(collisions, CoordGrid(0, 0), 1, Direction.NORTH) } returns true
        every { strategy.blocked(collisions, CoordGrid(0, 2), 1, Direction.SOUTH) } returns true
        assertEquals(listOf(CoordGrid(0, 0), CoordGrid(0, 2)), graph.getCenterPoints(strategy, CoordGrid(0).toCuboid(width = 2, height = 4)))
    }

    @Test
    fun `Find one point for two connected tiles`() {
        assertEquals(listOf(CoordGrid(0, 0)), graph.getCenterPoints(strategy, CoordGrid(0).toCuboid(width = 2, height = 2)))
    }

    @Test
    fun `Find two points for two separated knots of three tiles each`() {
        for(x in 0 until 3) {
            every { strategy.blocked(collisions, CoordGrid(x, 1), 1, Direction.NONE) } returns true
            every { strategy.blocked(collisions, CoordGrid(x, 0), 1, Direction.NORTH) } returns true
            every { strategy.blocked(collisions, CoordGrid(x, 0), 1, Direction.NORTH_EAST) } returns true
            every { strategy.blocked(collisions, CoordGrid(x, 0), 1, Direction.NORTH_WEST) } returns true
            every { strategy.blocked(collisions, CoordGrid(x, 2), 1, Direction.SOUTH) } returns true
            every { strategy.blocked(collisions, CoordGrid(x, 2), 1, Direction.SOUTH_EAST) } returns true
            every { strategy.blocked(collisions, CoordGrid(x, 2), 1, Direction.SOUTH_WEST) } returns true
        }
        assertEquals(listOf(CoordGrid(1, 0), CoordGrid(1, 2)), graph.getCenterPoints(strategy, CoordGrid(0).toCuboid(width = 3, height = 3)))
    }

    @Test
    fun `Find closest free tile to center point`() {
        val center = CoordGrid(1, 1)
        every { strategy.blocked(collisions, center, 1, Direction.NONE) } returns true
        for(dir in Direction.all) {
            every { strategy.blocked(collisions, center.minus(dir.delta), 1, dir) } returns true
        }
        assertEquals(listOf(CoordGrid(0, 1)), graph.getCenterPoints(strategy, CoordGrid(0).toCuboid(width = 3, height = 3)))
    }

    @Test
    fun `Link between two points`() {
        val points = setOf(CoordGrid(0, 0), CoordGrid(1, 0))
        val results = graph.getStaticLinks(strategy, points, 2)
        assertEquals(setOf(Triple(CoordGrid(0, 0), CoordGrid(1, 0), 1), Triple(CoordGrid(1, 0), CoordGrid(0, 0), 1)), results)
    }

    @Test
    fun `Don't link points outside of area`() {
        val points = setOf(CoordGrid(0, 0), CoordGrid(6, 0))
        val results = graph.getStaticLinks(strategy, points, 2)
        assertEquals(setOf<Triple<CoordGrid, CoordGrid, Int>>(), results)
    }

    @Test
    fun `Link between multiple points`() {
        val points = setOf(CoordGrid(0, 0), CoordGrid(3, 3), CoordGrid(1, 1))
        val results = graph.getStaticLinks(strategy, points, 4)
        assertEquals(setOf(
            Triple(CoordGrid(0, 0), CoordGrid(1, 1), 1),
            Triple(CoordGrid(0, 0), CoordGrid(3, 3), 3),
            Triple(CoordGrid(3, 3), CoordGrid(1, 1), 2),
            Triple(CoordGrid(3, 3), CoordGrid(0, 0), 3),
            Triple(CoordGrid(1, 1), CoordGrid(0, 0), 1),
            Triple(CoordGrid(1, 1), CoordGrid(3, 3), 2)
        ), results)
    }

    @Test
    fun `Get unlinked point`() {
        val points = setOf(CoordGrid(0, 0))
        val links = setOf(Triple(CoordGrid(1, 1), CoordGrid(2, 2), 1))
        val results = graph.getUnlinkedPoints(points, links)
        assertEquals(points, results)
    }

    @Test
    fun `Unidirectional point isn't unlinked`() {
        val points = setOf(CoordGrid(0, 0))
        val links = setOf(Triple(CoordGrid(0, 0), CoordGrid(1, 1), 1))
        val results = graph.getUnlinkedPoints(points, links)
        assertEquals(setOf<CoordGrid>(), results)
    }

    @Test
    fun `Unidirectional inverse point isn't unlinked`() {
        val points = setOf(CoordGrid(0, 0))
        val links = setOf(Triple(CoordGrid(1, 1), CoordGrid(0, 0), 1))
        val results = graph.getUnlinkedPoints(points, links)
        assertEquals(setOf<CoordGrid>(), results)
    }

    @Test
    fun `Identify portals`() {
        val objects = mutableSetOf<GameObject>()
        val results = graph.getPortals(objects)
        assertEquals(setOf<CoordGrid>(), results)
    }

    @Test
    fun `Get duplicate paths`() {
        val links = setOf(
            Triple(CoordGrid(0, 0), CoordGrid(1, 0), 1),
            Triple(CoordGrid(1, 0), CoordGrid(0, 1), 1),
            Triple(CoordGrid(0, 1), CoordGrid(0, 0), 1)
        )
        val results = graph.getDuplicatePaths(links)
        assertEquals(setOf(Triple(CoordGrid(0, 0), CoordGrid(1, 0), 1)), results)
    }
}