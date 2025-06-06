package world.gregs.voidps.tools.convert

import world.gregs.voidps.cache.CacheDelegate
import java.io.File

import com.google.gson.GsonBuilder
import world.gregs.voidps.cache.definition.data.NPCDefinitionFull

object NPCDecoderTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val other = File("./data941-jun 5 2025/")
        val otherCache = CacheDelegate(other.path)
        val npcDefinitionsRS3 = NPCDecoderRS3().load(otherCache)
        write(npcDefinitionsRS3, File("./rs3defs.json"))
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


