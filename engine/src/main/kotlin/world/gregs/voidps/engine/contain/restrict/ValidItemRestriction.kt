package world.gregs.voidps.engine.contain.restrict

import world.gregs.voidps.engine.data.definition.extra.ItemDefinitions

class ValidItemRestriction(
    private val definitions: ItemDefinitions
) : ItemRestrictionRule {
    override fun restricted(id: String): Boolean {
        return id.isBlank() || !definitions.contains(id)
    }
}