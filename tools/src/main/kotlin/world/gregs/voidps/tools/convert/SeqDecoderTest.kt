package world.gregs.voidps.tools.convert

import com.google.gson.GsonBuilder
import world.gregs.voidps.cache.CacheDelegate
import world.gregs.voidps.cache.definition.data.AnimationDefinitionFull
import world.gregs.voidps.cache.definition.data.NPCDefinitionFull
import world.gregs.voidps.cache.definition.decoder.AnimationDecoderFull
import world.gregs.voidps.cache.definition.decoder.NPCDecoderFull
import java.io.File

object SeqDecoderTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val cache634 = File("./data/cache/")
        if (cache634.exists()) {
            val otherCache = CacheDelegate(cache634.path)
            val npcDefinitionsRS3 = AnimationDecoderFull().load(otherCache)
            write(npcDefinitionsRS3, File("./rs2defseq.json"))
        }
        val other = File("K:\\documents\\GitHub\\rs3downloader\\data 941 mult\\")
        if (other.exists()) {
            val otherCache = CacheDelegate(other.path)
            val npcDefinitionsRS3 = AnimationDecoderFull().load(otherCache)
            write(npcDefinitionsRS3, File("./rs3defseq.json"))
        }
    }

    fun write(array: Array<AnimationDefinitionFull>, outputFile: File) {
        val gson = GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create()

        val json = gson.toJson(array)
        outputFile.writeText(json)
    }
}


