package world.gregs.voidps.engine.data.definition.data

import world.gregs.voidps.engine.entity.item.Item

/**
 * @param level mining level required to smelt
 * @param xp experience for successful smelting
 * @param chance of success out of 255
 * @param items required items to smelt
 */
data class Smelting(
    val level: Int = 0,
    val xp: Double = 0.0,
    val chance: Int = 255,
    val items: List<Item> = emptyList(),
    val message: String = ""
) {
    companion object {
        @Suppress("UNCHECKED_CAST")
        operator fun invoke(map: Map<String, Any>) = Smelting(
            level = map["level"] as Int,
            xp = map["xp"] as Double,
            chance = (map["chance"] as? IntRange)?.last ?: EMPTY.chance,
            items = (map["items"] as List<Map<String, Any>>).map { Item(it["item"] as String, it["amount"] as? Int ?: 1) },
            message = map["message"] as String
        )

        val EMPTY = Smelting()
    }
}