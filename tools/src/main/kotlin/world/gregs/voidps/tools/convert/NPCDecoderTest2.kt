package world.gregs.voidps.tools.convert

import world.gregs.voidps.cache.CacheDelegate
import java.io.File

import com.google.gson.GsonBuilder
import world.gregs.voidps.cache.definition.data.NPCDefinitionFull
import world.gregs.voidps.cache.definition.decoder.NPCDecoderFull

object NPCDecoderTest2 {

    @JvmStatic
    fun main(args: Array<String>) {
        val other = File("./data/cache/")
        val otherCache = CacheDelegate(other.path)
        val npcDefinitionsRS3 = NPCDecoderFull().load(otherCache)
        write(npcDefinitionsRS3, File("./rs2defs.json"))
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


