package world.gregs.voidps.tools.map.obj

import world.gregs.voidps.cache.Cache
import world.gregs.voidps.cache.definition.data.ClientScriptDefinition
import world.gregs.voidps.cache.definition.data.WorldMapDefinition
import world.gregs.voidps.cache.definition.data.WorldMapIconDefinition
import world.gregs.voidps.type.CoordGrid
import world.gregs.voidps.tools.ClientScriptDefinitions

class WorldMapDungeons(
    private val detailsDecoder: Array<WorldMapDefinition>,
    private val iconDecoder: Array<WorldMapIconDefinition>,
    private val scriptDecoder: Array<ClientScriptDefinition>,
    private val cache: Cache
) {

    fun getLinks(): List<Pair<CoordGrid, CoordGrid>> {
        val list = mutableListOf<Pair<CoordGrid, CoordGrid>>()
        for (i in detailsDecoder.indices) {
            val def = detailsDecoder.getOrNull(i) ?: continue
            val iconDef = iconDecoder.getOrNull(def.map.hashCode()) ?: continue
            iconDef.icons.forEach { (id, position) ->
                val scriptId = ClientScriptDefinitions.getScriptId(cache, id, 10)
                val script = scriptDecoder[scriptId]
                if (script.hasInstruction(0, LOCATION) && script.hasInstruction(1, SCRIPT_ID) && script.getIntOrNull(1) == 304) {
                    val targetPos = script.getInt(0)

                    when (position) {
                        // Manual fixes
                        CoordGrid.id(2827, 3646) -> {
                            // entrance can only be used during troll stronghold quest
                        }
                        CoordGrid.id(2998, 3376) -> {
//                            val link = graph.addLink(Tile(position), Tile(targetPos))
//                            link.actions = mutableListOf("item 952 Dig")
                        }
                        CoordGrid.id(2163, 5115) -> list.add(CoordGrid(2162, 5112, 1) to CoordGrid(targetPos))
                        CoordGrid.id(3821, 9462) -> {
                            list.add(CoordGrid(3815, 9463) to CoordGrid(3815, 3063))
                            list.add(CoordGrid(3830, 9463) to CoordGrid(3830, 3063))
                        }
                        CoordGrid.id(3821, 3062) -> {
                            list.add(CoordGrid(3815, 3063) to CoordGrid(3815, 9463))
                            list.add(CoordGrid(3830, 3063) to CoordGrid(3830, 9463))
                        }
                        else -> list.add(CoordGrid(position) to CoordGrid(targetPos))
                    }
                }
            }
        }
        return list
    }

    companion object {
        private fun ClientScriptDefinition.hasInstruction(index: Int, type: Int) = instructions.getOrNull(index) == type
        private fun ClientScriptDefinition.getIntOrNull(index: Int) = intOperands?.getOrNull(index)
        private fun ClientScriptDefinition.getInt(index: Int) = intOperands!![index]
        private const val LOCATION = 0
        private const val SCRIPT_ID = 40
    }
}