package world.gregs.voidps.tools.convert

import world.gregs.voidps.buffer.write.BufferWriter
import world.gregs.voidps.cache.*
import world.gregs.voidps.cache.Index.ITEMS
import world.gregs.voidps.cache.Index.NPCS
import world.gregs.voidps.cache.Index.OBJECTS
import world.gregs.voidps.cache.definition.Parameterized
import world.gregs.voidps.cache.definition.data.NPCDefinitionFull
import world.gregs.voidps.cache.definition.decoder.ItemDecoderFull
import world.gregs.voidps.cache.definition.decoder.NPCDecoderFull
import world.gregs.voidps.cache.definition.decoder.ObjectDecoderFull
import world.gregs.voidps.cache.definition.encoder.ItemEncoder
import world.gregs.voidps.cache.definition.encoder.NPCEncoder
import world.gregs.voidps.cache.definition.encoder.ObjectEncoder
import world.gregs.voidps.type.Direction
import java.io.File

object DefinitionsSpawnDirectionConverter {

    fun convert(target: File, other: File) {
        val cache = CacheDelegate(target.path)
        val otherCache = CacheDelegate(other.path)

        val npcDefinitions718 = NPCDecoder718(extendedTransforms = true).load(otherCache)

        val npcDecoder = NPCDecoderFull(members = false)
        val npcDefinitions = npcDecoder.load(cache)
        val npcDecoderMembers = NPCDecoderFull(members = true)
        val npcDefinitionsMembers = npcDecoderMembers.load(cache)
        val npcEncoder = NPCEncoder()
        val npcCount = definition(npcDecoder, npcEncoder, npcDefinitions, npcDefinitionsMembers, npcDefinitions718, cache, NPCS)
        println("Parameters transferred from $npcCount npc definitions.")

        println("Writing changes to cache...")
//        cache.update()
    }

    private fun definition(
        decoder: DefinitionDecoder<NPCDefinitionFull>,
        encoder: DefinitionEncoder<NPCDefinitionFull>,
        definitions: Array<NPCDefinitionFull>,
        members: Array<NPCDefinitionFull>,
        definitions718: Array<NPCDefinitionFull>,
        cache: Cache,
        index: Int
    ): Int {
        var count = 0
        for (id in definitions.indices) {
            val def = definitions.getOrNull(id) ?: continue
            val membersDef = members.getOrNull(id) ?: continue
            val def718 = definitions718.getOrNull(id) ?: continue
            val respawnRs3 = def718.respawnDirection
            val respawn = def.respawnDirection

            var modified = false
            //3874 are diff
            //3812 are diff and not south.
            //27110 are identical

            //30392 of the npc defs in 634 are south. This is out of 30984.
            //30392 of the npc defs in 634 are south.

            //if we overlap ids, then 26649 rs3 are south.
            //13503 only have matching names.

            //1023 are diff with matching names
            //1019 are diff with matching names and not south rotation? So there are 4 that are o.o
            if(respawn != respawnRs3 && respawnRs3 != Direction.SOUTH && def718.name.toLowerCase().equals(def.name.toLowerCase())) {
                def.respawnDirection = respawnRs3
                modified = true
            }

            if (modified) {
//                val writer = BufferWriter(capacity = 2048)
//                with(encoder) {
//                    writer.encode(def, membersDef)
//                }
//                cache.write(index, decoder.getArchive(id), decoder.getFile(id), writer.toArray())
                count++
            }
        }
        return count
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val cache = File("./data/cache/")
        val other = File("./data941-jun 5 2025/")
        convert(cache, other)
    }
}