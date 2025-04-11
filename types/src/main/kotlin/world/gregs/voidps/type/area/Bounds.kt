package world.gregs.voidps.type.area

import world.gregs.voidps.type.*

data class Bounds(//bounds
    val minX: Int,
    val minY: Int,
    val maxX: Int,
    val maxY: Int
) : Area {

    constructor(tile: Tile, width: Int, height: Int) : this(tile.x, tile.y, tile.x + width - 1, tile.y + height - 1)

    override val area: Double
        get() = (width * height).toDouble()
    val width: Int
        get() = maxX - minX + 1
    val height: Int
        get() = maxY - minY + 1

    override fun toRegions(): List<MapSquareKey> {
        val list = mutableListOf<MapSquareKey>()
        val max = Tile(maxX, maxY).region
        val min = Tile(minX, minY).region
        for (x in min.x..max.x) {
            for (y in min.y..max.y) {
                list.add(MapSquareKey(x, y))
            }
        }
        return list
    }

    override fun toZones(level: Int): List<ZoneKey> {
        val list = mutableListOf<ZoneKey>()
        val max = Tile(maxX, maxY).zone
        val min = Tile(minX, minY).zone
        for (x in min.x..max.x) {
            for (y in min.y..max.y) {
                list.add(ZoneKey(x, y, level))
            }
        }
        return list
    }

    fun toZonesReversed(level: Int): List<ZoneKey> {
        val list = mutableListOf<ZoneKey>()
        val max = Tile(maxX, maxY).zone
        val min = Tile(minX, minY).zone
        for (y in min.y..max.y) {
            for (x in min.x..max.x) {
                list.add(ZoneKey(x, y, level))
            }
        }
        return list
    }

    fun intersects(other: Bounds): Boolean {
        if (other.width <= 0 || other.height <= 0 || width <= 0 || height <= 0) {
            return false
        }
        return (other.maxX <= other.minX || other.maxX - 1 > minX) &&
                (other.maxY <= other.minY || other.maxY - 1 > minY) &&
                (maxX <= minX || maxX - 1 > other.minX) &&
                (maxY <= minY || maxY - 1 > other.minY)
    }

    override fun contains(x: Int, y: Int, level: Int): Boolean {
        return x in minX..maxX && y in minY..maxY
    }

    override fun random() = Tile(if (minX == maxX) minX else random.nextInt(minX, maxX + 1), if (minY == maxY) minY else random.nextInt(minY, maxY + 1), 0)


    override fun toString(): String {
        return "Rectangle($minX..$maxX, $minY..$maxY)"
    }

    override fun iterator(): Iterator<Tile> {
        val tile = Tile(minX, minY)
        return object : Iterator<Tile> {
            private var index = 0

            override fun hasNext() = index < area

            override fun next() = tile.add(x = index / height, y = index++.rem(height))
        }
    }
}