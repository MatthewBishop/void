package world.gregs.voidps.type

import world.gregs.voidps.type.area.Cuboid

@JvmInline
value class MapSquareGrid(val id: Int) {//MapSquareGrid

    val x: Int
        get() = x(id)
    val y: Int
        get() = y(id)
    val level: Int
        get() = level(id)
    val region: MapSquareKey
        get() = MapSquareKey(x, y)
    val zone: ZoneKey
        get() = ZoneKey(x shl 3, y shl 3, level)
    val tile: CoordGrid
        get() = CoordGrid(x shl 6, y shl 6, level)

    fun copy(x: Int = this.x, y: Int = this.y, level: Int = this.level) = MapSquareGrid.fromAbsolute(x, y, level)

    fun toCuboid(width: Int = 1, height: Int = 1, levels: Int = 1) = Cuboid(tile, width * 64, height * 64, levels)
    fun toCuboid(radius: Int, levels: Int = 1) = Cuboid(minus(radius, radius).tile, (radius * 2 + 1) * 64, (radius * 2 + 1) * 64, levels)

    companion object {
        public fun fromAbsolute(x: Int, z: Int, level: Int): MapSquareGrid =
            MapSquareGrid(
                id(x, z, level))
        fun id(x: Int, y: Int, level: Int) = (y and 0xff) + ((x and 0xff) shl 8) + ((level and 0x3) shl 16)
        fun x(id: Int) = id shr 8 and 0xff
        fun y(id: Int) = id and 0xff
        fun level(id: Int) = id shr 16
        val EMPTY = MapSquareGrid.fromAbsolute(0, 0, 0)
    }

    fun add(x: Int = 0, y: Int = 0, level: Int = 0) = copy(this.x + x, this.y + y, this.level + level)
    fun minus(x: Int = 0, y: Int = 0, level: Int = 0) = add(-x, -y, -level)
    fun delta(x: Int = 0, y: Int = 0, level: Int = 0) = Delta(this.x - x, this.y - y, this.level - level)

    fun add(value: Delta) = add(value.x, value.y, value.level)
    fun delta(value: MapSquareGrid) = delta(value.x, value.y, value.level)
    fun add(direction: Direction) = add(direction.delta)
}
fun MapSquareGrid.equals(x: Int = 0, y: Int = 0, level: Int = 0) = this.x == x && this.y == y && this.level == level