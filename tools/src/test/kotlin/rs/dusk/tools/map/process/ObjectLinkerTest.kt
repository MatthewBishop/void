package rs.dusk.tools.map.process

import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import rs.dusk.engine.entity.Size
import rs.dusk.engine.entity.obj.GameObject
import rs.dusk.engine.map.Tile
import rs.dusk.engine.path.TargetStrategy
import rs.dusk.tools.map.obj.ObjectLinker

internal class ObjectLinkerTest {

    lateinit var linking: ObjectLinker
    lateinit var interactOne: TargetStrategy
    lateinit var interactTwo: TargetStrategy
    lateinit var sizeOne: Size
    lateinit var sizeTwo: Size
    lateinit var one: GameObject
    lateinit var two: GameObject

    @BeforeEach
    fun setup() {
        linking = ObjectLinker(mockk())
        interactOne = mockk(relaxed = true)
        interactTwo = mockk(relaxed = true)
        sizeOne = mockk(relaxed = true)
        sizeTwo = mockk(relaxed = true)
        one = mockk(relaxed = true)
        two = mockk(relaxed = true)

        every { one.size } returns sizeOne
        every { two.size } returns sizeTwo
        every { one.interactTarget } returns interactOne
        every { two.interactTarget } returns interactTwo
    }

    @Test
    fun `Can't interact with one then no delta`() {
        every { interactOne.reached(any(), any()) } returns false
        val delta = linking.deltaBetween(one, two)
        assertNull(delta)
    }

    @Test
    fun `Can't interact with two then no delta`() {
        every { interactOne.reached(any(), any()) } returns true
        every { interactTwo.reached(any(), any()) } returns false
        val delta = linking.deltaBetween(one, two)
        assertNull(delta)
    }

    /**
     *  |b| | |
     *  | |2| |
     *  | | | |
     *  |a| | |
     *  | |1| |
     *  | | | |
     */
    @Test
    fun `Small objects link on the same side`() {
        every { one.tile } returns Tile(1, 1)
        every { two.tile } returns Tile(1, 501)
        every { sizeOne.width } returns 1
        every { sizeTwo.width } returns 1
        every { sizeOne.height } returns 1
        every { sizeTwo.height } returns 1
        every { interactOne.reached(any(), any()) } returns true
        every { interactTwo.reached(any(), any()) } returns true

        val delta = linking.deltaBetween(one, two)
        assertNotNull(delta)
        assertEquals(0, delta!!.x)
        assertEquals(500, delta.y)
        assertEquals(0, delta.plane)
    }

    /**
     *  | | | |
     *  | |2| |
     *  | |b| |
     *  | | | |
     *  | |1| |
     *  | |a| |
     */
    @Test
    fun `Small objects link on first walkable side`() {
        every { one.tile } returns Tile(1, 1)
        every { two.tile } returns Tile(1, 501)
        every { sizeOne.width } returns 1
        every { sizeTwo.width } returns 1
        every { sizeOne.height } returns 1
        every { sizeTwo.height } returns 1
        every { interactOne.reached(any(), any()) } returns false
        every { interactTwo.reached(any(), any()) } returns false
        every { interactOne.reached(Tile(1, 2), any()) } returns true
        every { interactOne.reached(Tile(1, 0), any()) } returns true
        every { interactTwo.reached(Tile(2, 501), any()) } returns true
        every { interactTwo.reached(Tile(1, 500), any()) } returns true

        val delta = linking.deltaBetween(one, two)
        assertNotNull(delta)
        assertEquals(0, delta!!.x)
        assertEquals(500, delta.y)
        assertEquals(0, delta.plane)
    }

    /**
     *  | | | | |
     *  | |2|2|b|
     *  | | | | |
     *  | | | | |
     *  |a|1|1| |
     *  | | | | |
     */
    @Test
    fun `Rectangle objects link on opposite horizontal side`() {
        every { one.tile } returns Tile(1, 1)
        every { two.tile } returns Tile(1, 501)
        every { sizeOne.width } returns 2
        every { sizeTwo.width } returns 2
        every { sizeOne.height } returns 1
        every { sizeTwo.height } returns 1
        every { interactOne.reached(any(), any()) } returns false
        every { interactTwo.reached(any(), any()) } returns false
        every { interactOne.reached(Tile(0, 1), any()) } returns true
        every { interactTwo.reached(Tile(3, 501), any()) } returns true

        val delta = linking.deltaBetween(one, two)
        assertNotNull(delta)
        assertEquals(3, delta!!.x)
        assertEquals(500, delta.y)
        assertEquals(0, delta.plane)
    }

    /**
     *  | | | |b|
     *  | | |2|2|
     *  | | |2|2|
     *  | | | | |
     *  | | | | |
     *  | |1|1| |
     *  | |1|1| |
     *  | | |a| |
     */
    @Test
    fun `Rectangle objects link on opposite vertical side`() {
        every { one.tile } returns Tile(1, 1)
        every { two.tile } returns Tile(2, 1)
        every { sizeOne.width } returns 2
        every { sizeTwo.width } returns 2
        every { sizeOne.height } returns 2
        every { sizeTwo.height } returns 2
        every { interactOne.reached(any(), any()) } returns false
        every { interactTwo.reached(any(), any()) } returns false
        every { interactOne.reached(Tile(2, 0), any()) } returns true
        every { interactTwo.reached(Tile(3, 3), any()) } returns true

        val delta = linking.deltaBetween(one, two)
        assertNotNull(delta)
        assertEquals(1, delta!!.x)
        assertEquals(3, delta.y)
        assertEquals(0, delta.plane)
    }

}