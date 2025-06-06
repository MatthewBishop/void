package world.gregs.voidps.tools.convert

import world.gregs.voidps.cache.CacheDelegate
import java.io.File

object NPCDecoderTest {

    @JvmStatic
    fun main(args: Array<String>) {
        val other = File("./data941-jun 5 2025/")
        val otherCache = CacheDelegate(other.path)
        val npcDefinitionsRS3 = NPCDecoderRS3().load(otherCache)

    }
}