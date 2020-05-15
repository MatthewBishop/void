package rs.dusk.engine.model.entity.index.update.visual

import rs.dusk.engine.model.entity.index.npc.NPC
import rs.dusk.engine.model.entity.index.player.Player
import rs.dusk.engine.model.entity.index.update.Visual

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @since April 25, 2020
 */
data class Hits(
    val hits: MutableList<Hit> = mutableListOf(),
    var source: Int = -1,// TODO source & target setting
    var target: Int = -1
) : Visual

const val PLAYER_HITS_MASK = 0x4

const val NPC_HITS_MASK = 0x40

fun Player.flagHits() = visuals.flag(PLAYER_HITS_MASK)

fun NPC.flagHits() = visuals.flag(NPC_HITS_MASK)

fun Player.getHits() = visuals.getOrPut(PLAYER_HITS_MASK) { Hits() }

fun NPC.getHits() = visuals.getOrPut(NPC_HITS_MASK) { Hits() }

fun Player.addHit(hit: Hit) {
    val hits = getHits()
    hits.hits.add(hit)
    flagHits()
}

fun NPC.addHit(hit: Hit) {
    val hits = getHits()
    hits.hits.add(hit)
    flagHits()
}