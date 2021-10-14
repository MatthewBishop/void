package world.gregs.voidps.world.activity.skill.woodcutting.log

import world.gregs.voidps.engine.utility.toTitleCase
import world.gregs.voidps.engine.utility.toUnderscoreCase

enum class JadinkoRoot : Log {
    StraightRoot,
    CurlyRoot;

    override val id: String = name.toTitleCase().toUnderscoreCase()
}