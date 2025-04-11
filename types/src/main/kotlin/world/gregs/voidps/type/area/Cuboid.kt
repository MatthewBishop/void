package world.gregs.voidps.type.area

import world.gregs.voidps.type.*

data class Cuboid(
    val minX: Int,
    val minY: Int,
    val maxX: Int = minX,
    val maxY: Int = minY,
    val minLevel: Int = 0,
    val maxLevel: Int = minLevel
) : Area {

    constructor(tile: Tile, width: Int, height: Int, levels: Int) : this(tile.x, tile.y, tile.x + width - 1, tile.y + height - 1, tile.level, tile.level + levels - 1)

    override val area: Double
        get() = (width * height * levels).toDouble()

    val width: Int
        get() = maxX - minX + 1

    val height: Int
        get() = maxY - minY + 1

    val levels: Int
        get() = maxLevel - minLevel + 1

    override fun toRegions(): List<MapSquareKey> {
        val list = mutableListOf<MapSquareKey>()
        val max = Tile(maxX, maxY, maxLevel).region
        val min = Tile(minX, minY, minLevel).region
        for (x in min.x..max.x) {
            for (y in min.y..max.y) {
                list.add(MapSquareKey(x, y))
            }
        }
        return list
    }

    override fun toZones(level: Int): List<ZoneKey> {
        val list = mutableListOf<ZoneKey>()
        val max = Tile(maxX, maxY, maxLevel).zone
        val min = Tile(minX, minY, minLevel).zone
        for (lvl in min.level..max.level) {
            for (x in min.x..max.x) {
                for (y in min.y..max.y) {
                    list.add(ZoneKey(x, y, lvl))
                }
            }
        }
        return list
    }

    fun toRegionLevels(): List<MapSquareGrid> {
        val list = mutableListOf<MapSquareGrid>()
        val max = Tile(maxX, maxY, maxLevel).regionLevel
        val min = Tile(minX, minY, minLevel).regionLevel
        for (level in min.level..max.level) {
            for (x in min.x..max.x) {
                for (y in min.y..max.y) {
                    list.add(MapSquareGrid(x, y, level))
                }
            }
        }
        return list
    }

    fun toRectangles(): List<Bounds> = (minLevel..maxLevel).map { Bounds(minX, minY, maxX, maxY) }

    override fun contains(x: Int, y: Int, level: Int): Boolean {
        return level in minLevel..maxLevel && x in minX..maxX && y in minY..maxY
    }

    override fun random() = Tile(random(minX, maxX), random(minY, maxY), random(minLevel, maxLevel))

    companion object {
        fun random(first: Int, second: Int) = if (first == second) first else random.nextInt(first, second + 1)
    }

    override fun toString(): String {
        return "Cuboid($minX..$maxX, $minY..$maxY, $minLevel..$maxLevel)"
    }

    override fun iterator(): Iterator<Tile> {
        val tile = Tile(minX, minY, minLevel)
        return object : Iterator<Tile> {
            private val max = area
            private var index = 0

            override fun hasNext() = index < max

            override fun next(): Tile {
                val coords = tile.add(
                    x = index.rem(width * height) / height,
                    y = index.rem(width * height).rem(height),
                    level = index / (width * height)
                )
                index++
                return coords
            }
        }
    }
}