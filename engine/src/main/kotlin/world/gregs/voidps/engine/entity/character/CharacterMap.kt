package world.gregs.voidps.engine.entity.character

import world.gregs.voidps.type.MapSquareGrid

class CharacterMap {

    val regions = arrayOfNulls<MutableList<Int>?>(256 * 256 * 4)

    fun add(region: MapSquareGrid, character: Character) {
        if (regions[region.id] == null) {
            regions[region.id] = ArrayList(8)
        }
        regions[region.id]!!.add(character.index)
    }

    fun remove(region: MapSquareGrid, character: Character) {
        val list = regions[region.id] ?: return
        list.remove(character.index)
    }

    operator fun get(region: MapSquareGrid): List<Int>? {
        return regions[region.id]
    }
}