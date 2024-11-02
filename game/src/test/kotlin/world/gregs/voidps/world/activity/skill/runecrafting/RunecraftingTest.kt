package world.gregs.voidps.world.activity.skill.runecrafting

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DynamicTest.dynamicTest
import org.junit.jupiter.api.TestFactory
import org.koin.test.get
import world.gregs.voidps.engine.entity.character.player.skill.Skill
import world.gregs.voidps.engine.inv.add
import world.gregs.voidps.engine.inv.inventory
import world.gregs.voidps.type.CoordGrid
import world.gregs.voidps.world.interact.entity.obj.Teleports
import world.gregs.voidps.world.script.WorldTest
import world.gregs.voidps.world.script.itemOnObject
import world.gregs.voidps.world.script.objectOption

internal class RunecraftingTest : WorldTest() {

    private lateinit var teleports: Teleports

    @BeforeEach
    fun setup() {
        teleports = get()
    }

    @TestFactory
    fun `Craft runes with rune essence`() = altars.filter { !it.pure }.map { (type, _, altarTile) ->
        dynamicTest("Craft $type runes with rune essence") {
            val tile = teleports.get("${type}_altar_ruins_enter", "Enter").first().to
            val player = createPlayer("player", tile)
            player.levels.set(Skill.Runecrafting, 99)
            player.inventory.add("rune_essence")

            val altar = objects[altarTile, "${type}_altar"]!!
            player.objectOption(altar, "Craft-rune")
            tick(1)
            tickIf { player.visuals.moved }

            assertFalse(player.inventory.contains("rune_essence"))
            assertTrue(player.inventory.contains("${type}_rune"))
            assertTrue(player.experience.get(Skill.Runecrafting) > 0)
        }
    }

    @TestFactory
    fun `Cant craft high level runes with rune essence`() = altars.filter { it.pure }.map { (type, _, altarTile) ->
        dynamicTest("Can't craft $type runes with rune essence") {
            val tile = teleports.get("${type}_altar_ruins_enter", "Enter").first().to
            val player = createPlayer("player", tile)
            player.levels.set(Skill.Runecrafting, 99)
            player.inventory.add("rune_essence")

            val altar = objects[altarTile, "${type}_altar"]!!
            player.objectOption(altar, "Craft-rune")
            tick(1)
            tickIf { player.visuals.moved }

            assertTrue(player.inventory.contains("rune_essence"))
            assertFalse(player.inventory.contains("${type}_rune"))
            assertEquals(0.0, player.experience.get(Skill.Runecrafting))
        }
    }

    @TestFactory
    fun `Craft runes with pure essence`() = altars.map { (type, _, altarTile) ->
        dynamicTest("Craft $type runes with pure essence") {
            val tile = teleports.get("${type}_altar_ruins_enter", "Enter").first().to
            val player = createPlayer("player", tile)
            player.levels.set(Skill.Runecrafting, 99)
            player.inventory.add("pure_essence")

            val altar = objects[altarTile, "${type}_altar"]!!
            player.itemOnObject(altar, 0, "pure_essence")
            tick(1)
            tickIf { player.visuals.moved }

            assertFalse(player.inventory.contains("pure_essence"))
            assertTrue(player.inventory.contains("${type}_rune"))
            assertTrue(player.experience.get(Skill.Runecrafting) > 0)
        }
    }

    @TestFactory
    fun `Can craft multiple runes with one essence`() = altars.filter { it.type != "law" && it.type != "death" && it.type != "blood" }.map { (type, _, altarTile) ->
        dynamicTest("Craft multiple $type runes with pure essence") {
            val tile = teleports.get("${type}_altar_ruins_enter", "Enter").first().to
            val player = createPlayer("player", tile)
            player.levels.set(Skill.Runecrafting, 99)
            player.inventory.add("pure_essence")

            val altar = objects[altarTile, "${type}_altar"]!!
            player.itemOnObject(altar, 0, "pure_essence")
            tick(1)
            tickIf { player.visuals.moved }

            assertFalse(player.inventory.contains("pure_essence"))
            assertTrue(player.inventory.count("${type}_rune") > 1)
            assertTrue(player.experience.get(Skill.Runecrafting) > 0)
        }
    }

    @TestFactory
    fun `Cant craft runes without required level`() = altars.map { (type, _, altarTile) ->
        dynamicTest("Can't craft $type runes") {
            val tile = teleports.get("${type}_altar_ruins_enter", "Enter").first().to
            val player = createPlayer("player", tile)
            player.levels.set(Skill.Runecrafting, 0)
            player.inventory.add("pure_essence")

            val altar = objects[altarTile, "${type}_altar"]!!
            player.objectOption(altar, "Craft-rune")
            tick(1)
            tickIf { player.visuals.moved }

            assertTrue(player.inventory.contains("pure_essence"))
            assertFalse(player.inventory.contains("${type}_rune"))
            assertEquals(0.0, player.experience.get(Skill.Runecrafting))
        }
    }

    companion object {
        internal data class Altar(val type: String, val ruinsTile: CoordGrid, val altarTile: CoordGrid, val pure: Boolean = false)

        internal val altars = listOf(
            Altar("air", CoordGrid(3126, 3404), CoordGrid(2843, 4833)),
            Altar("water", CoordGrid(3184, 3164), CoordGrid(3483, 4835)),
            Altar("earth", CoordGrid(3305, 3473), CoordGrid(2657, 4840)),
            Altar("fire", CoordGrid(3312, 3254), CoordGrid(2584, 4837)),
            Altar("mind", CoordGrid(2981, 3513), CoordGrid(2785, 4840)),
            Altar("body", CoordGrid(3052, 3444), CoordGrid(2522, 4839)),
            Altar("cosmic", CoordGrid(2407, 4376), CoordGrid(2141, 4832), pure = true),
            Altar("law", CoordGrid(2857, 3380), CoordGrid(2463, 4831), pure = true),
            Altar("nature", CoordGrid(2868, 3018), CoordGrid(2399, 4840), pure = true),
            Altar("chaos", CoordGrid(3059, 3590), CoordGrid(2270, 4841), pure = true),
            Altar("death", CoordGrid(1860, 4638), CoordGrid(2204, 4835), pure = true),
            Altar("blood", CoordGrid(3560, 9780), CoordGrid(2461, 4894, 1), pure = true)
        )
    }

}