package world.gregs.void.engine.entity.character.update.visual

import world.gregs.void.engine.entity.character.npc.NPC
import world.gregs.void.engine.entity.character.player.Player
import world.gregs.void.engine.entity.character.update.Visual

/**
 * @author GregHib <greg@gregs.world>
 * @since April 25, 2020
 */
data class ColourOverlay(
    var delay: Int = 0,
    var duration: Int = 0,
    var colour: Int = 0
) : Visual

const val PLAYER_COLOUR_OVERLAY_MASK = 0x20000

const val NPC_COLOUR_OVERLAY_MASK = 0x200

fun Player.flagColourOverlay() = visuals.flag(PLAYER_COLOUR_OVERLAY_MASK)

fun NPC.flagColourOverlay() = visuals.flag(NPC_COLOUR_OVERLAY_MASK)

val Player.colourOverlay: ColourOverlay
    get() = visuals.getOrPut(PLAYER_COLOUR_OVERLAY_MASK) { ColourOverlay() }

val NPC.colourOverlay: ColourOverlay
    get() = visuals.getOrPut(NPC_COLOUR_OVERLAY_MASK) { ColourOverlay() }
