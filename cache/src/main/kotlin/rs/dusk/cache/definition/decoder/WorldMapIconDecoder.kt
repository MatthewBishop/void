package rs.dusk.cache.definition.decoder

import rs.dusk.buffer.read.BufferReader
import rs.dusk.buffer.read.Reader
import rs.dusk.cache.Cache
import rs.dusk.cache.DefinitionDecoder
import rs.dusk.cache.Indices.WORLD_MAP
import rs.dusk.cache.definition.data.WorldMapIcon
import rs.dusk.cache.definition.data.WorldMapIconDefinition

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @since December 21, 2020
 */
class WorldMapIconDecoder(cache: Cache) : DefinitionDecoder<WorldMapIconDefinition>(cache, WORLD_MAP) {

    private var archive = -1
    var aBoolean1313 = false

    override fun getArchive(id: Int) = archive

    override val size: Int
        get() = cache.lastFileId(index, archive)

    override fun create() = WorldMapIconDefinition()

    fun getOrNull(map: String): WorldMapIconDefinition? {
        archive = cache.getArchiveId(index, "${map}_staticelements")
        return super.getOrNull(map.hashCode())
    }

    fun get(map: String): WorldMapIconDefinition {
        archive = cache.getArchiveId(index, "${map}_staticelements")
        return super.get(map.hashCode())
    }

    override fun get(id: Int): WorldMapIconDefinition {
        throw IllegalAccessError("Use get(map: String)")
    }

    override fun readData(id: Int): WorldMapIconDefinition? {
        val archive = getArchive(id)
        var length = cache.archiveCount(index, archive)
        var counter = 0
        var index = 0
        if (length > 0) {
            val definition = create()
            definition.id = id
            val icons = mutableListOf<WorldMapIcon>()
            while (length > counter) {
                val data = getData(archive, index++)
                if (data != null) {
                    val buffer = BufferReader(data)
                    val position = buffer.readInt()
                    val iconId = buffer.readShort()
                    val skip = buffer.readUnsignedByte()
                    if (aBoolean1313 && skip == 1) {
                        length--
                    } else {
                        counter++
                        icons.add(WorldMapIcon(iconId, position))
                    }
                }
            }
            definition.icons = icons.toTypedArray()
            definition.changeValues()
            return definition
        }
        return null
    }

    override fun WorldMapIconDefinition.read(opcode: Int, buffer: Reader) {
    }
}