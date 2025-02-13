package world.gregs.voidps.type

import world.gregs.voidps.type.area.Cuboid

@JvmInline
value class MapSquareGrid(val id: Int) {//MapSquareGrid

    val x: Int
        get() = x(id)
    val z: Int
        get() = y(id)
    val level: Int
        get() = level(id)

    fun translate(x: Int = 0, y: Int = 0, level: Int = 0) = copy(this.x + x, this.z + y, this.level + level)
    fun minus(x: Int = 0, y: Int = 0, level: Int = 0) = translate(-x, -y, -level)
    fun plus(value: Delta) = translate(value.x, value.y, value.level)


    fun copy(x: Int = this.x, y: Int = this.z, level: Int = this.level) = MapSquareGrid.fromAbsolute(x, y, level)

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
}

//refactor: void specific vars are below.

val MapSquareGrid.tile: CoordGrid
    get() = CoordGrid(x shl 6, z shl 6, level)
fun MapSquareGrid.toCuboid(width: Int = 1, height: Int = 1, levels: Int = 1) = Cuboid(tile, width * 64, height * 64, levels)


fun MapSquareGrid.delta(x: Int = 0, y: Int = 0, level: Int = 0) = Delta(this.x - x, this.z - y, this.level - level)

fun MapSquareGrid.delta(value: MapSquareGrid) = delta(value.x, value.z, value.level)

fun MapSquareGrid.plus(direction: Direction) = plus(direction.delta)

fun MapSquareGrid.equals(x: Int = 0, y: Int = 0, level: Int = 0) = this.x == x && this.z == y && this.level == level

//test only

val MapSquareGrid.region: MapSquareKey //test only
    get() = MapSquareKey(x, z)
val MapSquareGrid.zone: ZoneKey //test only
    get() = ZoneKey(x shl 3, z shl 3, level)
fun MapSquareGrid.toCuboid(radius: Int, levels: Int = 1) = Cuboid(minus(radius, radius).tile, (radius * 2 + 1) * 64, (radius * 2 + 1) * 64, levels)
