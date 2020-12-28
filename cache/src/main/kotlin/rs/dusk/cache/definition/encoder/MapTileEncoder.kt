package rs.dusk.cache.definition.encoder

import rs.dusk.buffer.write.Writer
import rs.dusk.cache.DefinitionEncoder
import rs.dusk.cache.definition.data.MapDefinition

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @since December 28, 2020
 */
class MapTileEncoder : DefinitionEncoder<MapDefinition> {

    override fun Writer.encode(definition: MapDefinition) {
        if (definition.id == -1) {
            return
        }

        for (plane in 0 until 4) {
            for (localX in 0 until 64) {
                for (localY in 0 until 64) {
                    val tile = definition.getTile(localX, localY, plane)
                    if (tile.underlayId != 0) {
                        writeByte(tile.underlayId + 81)
                    }
                    if (tile.settings != 0) {
                        writeByte(tile.settings + 49)
                    }
                    if (tile.attrOpcode != 0) {
                        writeByte(tile.attrOpcode)
                        writeByte(tile.overlayId)
                    }
                    if (tile.height != 0) {
                        writeByte(1)
                        writeByte(tile.height)
                    }
                    writeByte(0)
                }
            }
        }
    }

}