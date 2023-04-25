package world.gregs.voidps.engine.client.variable

import world.gregs.voidps.engine.client.ui.chat.toInt
import world.gregs.voidps.engine.data.definition.config.VariableDefinition

/**
 * [VariableDefinition] formats for converting usable values into [Int]'s
 */
sealed class VariableValues {

    abstract fun default(): Any
    abstract fun toInt(value: Any): Int

    companion object {
        @Suppress("UNCHECKED_CAST")
        operator fun invoke(values: Any?, format: String?, default: Any?): VariableValues {
            return when (format ?: default!!::class.java.simpleName.lowercase()) {
                "int", "integer" -> IntValues
                "string" -> StringValues
                "double" -> DoubleValues
                "boolean" -> BooleanValues
                "list" -> ListValues(values as List<Any>, default)
                "map" -> MapValues(values as Map<Any, Int>, default)
                "bitwise" -> BitwiseValues(values as List<Any>)
                else -> NoValues
            }
        }
    }
}

object NoValues : VariableValues() {
    override fun default() = 0
    override fun toInt(value: Any) = -1
}

object IntValues : VariableValues() {
    override fun default() = 0
    override fun toInt(value: Any) = value as Int
}

object StringValues : VariableValues() {
    override fun default() = ""
    override fun toInt(value: Any) = -1
}

object DoubleValues : VariableValues() {
    override fun default() = 0.0
    override fun toInt(value: Any) = (value as Double).toInt() * 10
}

object BooleanValues : VariableValues() {
    override fun default() = false
    override fun toInt(value: Any) = (value as Boolean).toInt()
}

class ListValues(
    val values: List<Any>
) : VariableValues() {
    constructor(values: List<Any>, default: Any?) : this(values) {
        if (default != null) {
            check(values.contains(default)) { "List must contain default value '$default'" }
        }
    }

    override fun default() = values.first()
    override fun toInt(value: Any) = values.indexOf(value)
}

class MapValues(
    val values: Map<Any, Int>
) : VariableValues() {
    constructor(values: Map<Any, Int>, default: Any?) : this(values) {
        if (default != null) {
            check(values.containsKey(default)) { "Map must contain default value '$default'" }
        }
    }

    override fun default() = values.keys.first()
    override fun toInt(value: Any) =
        values[value] ?: if (value is Boolean) values[value.toString()] ?: -1 else -1

}

class BitwiseValues(
    val values: List<Any>
) : VariableValues() {

    override fun default() = arrayListOf<Any>()

    @Suppress("UNCHECKED_CAST")
    override fun toInt(value: Any) = (value as ArrayList<Any>).sumOf {
        val index = values.indexOf(it)
        if (index != -1) 1 shl index else 0
    }
}