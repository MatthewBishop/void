package world.gregs.voidps.type

import world.gregs.voidps.type.area.Cuboid
import world.gregs.voidps.type.area.Rectangle

/**
 * Represents a 8x8 tiled area
 */
@JvmInline
value class ZoneKey(val id: Int) {//this is a zonekey

    constructor(x: Int, y: Int, level: Int = 0) : this(id(x, y, level))

    val x: Int
        get() = x(id)
    val y: Int
        get() = y(id)
    val level: Int
        get() = level(id)
    val region: MapSquareKey
        get() = MapSquareKey(x shr 3, y shr 3)
    val regionLevel: MapSquareGrid
        get() = MapSquareGrid.fromAbsolute(x shr 3, y shr 3, level)
    val tile: CoordGrid
        get() = CoordGrid(x shl 3, y shl 3, level)

    fun copy(x: Int = this.x, y: Int = this.y, level: Int = this.level) = ZoneKey(x, y, level)

    fun safeMinus(zone: ZoneKey) = safeMinus(zone.x, zone.y, zone.level)
    fun safeMinus(x: Int = 0, y: Int = 0, level: Int = 0): ZoneKey {
        return ZoneKey((this.x - x).coerceAtLeast(0), (this.y - y).coerceAtLeast(0), (this.level - level).coerceAtLeast(0))
    }

    fun toRectangle(radius: Int) = Rectangle(safeMinus(radius, radius).tile, (radius * 2 + 1) * 8, (radius * 2 + 1) * 8)
    fun toRectangle(width: Int = 1, height: Int = 1) = Rectangle(tile, width * 8, height * 8)
    fun toCuboid(width: Int = 1, height: Int = 1) = Cuboid(tile, width * 8, height * 8, 1)
    fun toCuboid(radius: Int) = Cuboid(safeMinus(radius, radius).tile, (radius * 2 + 1) * 8, (radius * 2 + 1) * 8, 1)

    override fun toString(): String {
        return "Zone($x, $y, $level)"
    }

    companion object {
        fun id(x: Int, y: Int, level: Int) = (x and 0x7ff) + ((y and 0x7ff) shl 11) + ((level and 0x3) shl 22)
        fun x(id: Int) = id and 0x7ff
        fun y(id: Int) = id shr 11 and 0x7ff
        fun level(id: Int) = id shr 22 and 0x3
        val EMPTY = ZoneKey(0, 0, 0)

        /**
         * Index of a local tile within a zone
         */
        fun tileIndex(tileX: Int, tileY: Int, level: Int): Int = id(tileX shr 3, tileY shr 3, level)
    }

    fun add(x: Int = 0, y: Int = 0, level: Int = 0) = copy(this.x + x, this.y + y, this.level + level)
    fun minus(x: Int = 0, y: Int = 0, level: Int = 0) = add(-x, -y, -level)
    fun add(value: Delta) = add(value.x, value.y, value.level)
}

fun ZoneKey.equals(x: Int, y: Int, level: Int) = this.x == x && this.y == y && this.level == level