package world.gregs.voidps.type

enum class Direction(deltaX: Int, deltaY: Int) {//has the same name
    NORTH_WEST(-1, 1),
    NORTH(0, 1),
    NORTH_EAST(1, 1),
    EAST(1, 0),
    SOUTH_EAST(1, -1),
    SOUTH(0, -1),
    SOUTH_WEST(-1, -1),
    WEST(-1, 0),
    NONE(0, 0);

    val delta = Delta(deltaX, deltaY)

    fun isDiagonal() = delta.isHorizontal() && delta.isVertical()

    fun isCardinal(): Boolean = delta.isCardinal()

    fun isHorizontal() = delta.isHorizontal()

    fun isVertical() = delta.isVertical()

    /**
     * Rotate direction clockwise in increments of 1/8
     */
    fun rotate(count: Int): Direction {
        return all[(ordinal + count + all.size).rem(all.size)]
    }

    fun vertical(): Direction {
        return when (delta.y) {
            1 -> NORTH
            -1 -> SOUTH
            else -> NONE
        }
    }

    fun horizontal(): Direction {
        return when (delta.x) {
            1 -> EAST
            -1 -> WEST
            else -> NONE
        }
    }

    fun inverse(): Direction {
        return when (this) {
            NORTH_WEST -> SOUTH_EAST
            NORTH -> SOUTH
            NORTH_EAST -> SOUTH_WEST
            EAST -> WEST
            SOUTH_EAST -> NORTH_WEST
            SOUTH -> NORTH
            SOUTH_WEST -> NORTH_EAST
            WEST -> EAST
            NONE -> NONE
        }
    }

    companion object {
        val size = entries.size
        val cardinal = entries.filter { it.isCardinal() && it.delta.x != it.delta.y }
        val ordinal = entries.filter { it.isDiagonal() }
        val values = entries.toTypedArray()
        val reversed = entries.reversed()
        val all = entries.toTypedArray().copyOfRange(0, size - 1)
        val clockwise = arrayOf(NORTH, NORTH_EAST, EAST, SOUTH_EAST, SOUTH, SOUTH_WEST, WEST, NORTH_WEST)
        val westClockwise = arrayOf(WEST, NORTH, EAST, SOUTH)

        fun of(deltaX: Int, deltaY: Int): Direction {
            return all.firstOrNull { it.delta.equals(deltaX, deltaY) } ?: NONE
        }

        fun Int.getRespawnDirection(): Direction {
            return when (this) {
                0 -> Direction.NORTH
                1 -> Direction.NORTH_WEST
                2 -> Direction.NORTH_EAST
                3 -> Direction.SOUTH_WEST
                4 -> Direction.SOUTH
                5 -> Direction.WEST
                6 -> Direction.EAST
                7 -> Direction.SOUTH_EAST
                else -> throw IllegalArgumentException("Unsupported direction: $this")
            }
        }

        fun Direction.toRespawnDirectionInt(): Int = when (this) {
            Direction.NORTH -> 0
            Direction.NORTH_WEST -> 1
            Direction.NORTH_EAST -> 2
            Direction.SOUTH_WEST -> 3
            Direction.SOUTH -> 4
            Direction.WEST -> 5
            Direction.EAST -> 6
            Direction.SOUTH_EAST -> 7
            else -> throw IllegalArgumentException("Unsupported direction: $this")
        }
    }
}