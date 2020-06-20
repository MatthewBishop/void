package rs.dusk.tools

import org.koin.core.context.startKoin
import rs.dusk.cache.cacheDefinitionModule
import rs.dusk.cache.cacheModule
import rs.dusk.cache.definition.decoder.NPCDecoder

object NPCDefinitions {
    @JvmStatic
    fun main(args: Array<String>) {
        startKoin {
            fileProperties("/tool.properties")
            modules(cacheModule, cacheDefinitionModule)
        }
        val decoder = NPCDecoder(false)
        for (i in 0 until decoder.size) {
            val def = decoder.get(i) ?: continue
            if(def.name.equals("Alfonse", true)) {
                println("Found $i $def")
            }
        }
    }
}