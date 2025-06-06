package world.gregs.voidps.tools.convert

import com.google.gson.GsonBuilder
import world.gregs.voidps.cache.CacheDelegate
import world.gregs.voidps.cache.definition.data.NPCDefinitionFull
import world.gregs.voidps.cache.definition.decoder.NPCDecoderFull
import java.io.File

object NPCDecoderTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val cache634 = File("./data/cache/")
        if (cache634.exists()) {
            val otherCache = CacheDelegate(cache634.path)
            val npcDefinitionsRS3 = NPCDecoderFull().load(otherCache)
            write(npcDefinitionsRS3, File("./rs2defs.json"))
        }
        val other = File("./data941-jun 5 2025/")
        if (other.exists()) {
            val otherCache = CacheDelegate(other.path)
            val npcDefinitionsRS3 = NPCDecoder718(extendedTransforms = true).load(otherCache)
            write(npcDefinitionsRS3, File("./rs3defs.json"))
        }
    }

    fun write(array: Array<NPCDefinitionFull>, outputFile: File) {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()

        val json = gson.toJson(array)
        outputFile.writeText(json)
    }
}


