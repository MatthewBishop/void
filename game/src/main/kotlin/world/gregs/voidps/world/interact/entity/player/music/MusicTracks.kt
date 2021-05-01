package world.gregs.voidps.world.interact.entity.player.music

import org.koin.dsl.module
import world.gregs.voidps.engine.data.file.FileLoader
import world.gregs.voidps.engine.map.area.Area
import world.gregs.voidps.engine.map.area.Polygon
import world.gregs.voidps.engine.map.area.Rectangle
import world.gregs.voidps.engine.map.region.Region
import world.gregs.voidps.engine.map.region.RegionPlane
import world.gregs.voidps.utility.func.plural

val musicModule = module {
    single(createdAtStart = true) { MusicTracks(getProperty("musicPath"), get()) }
}

class MusicTracks(
    private val path: String,
    private val files: FileLoader
) {

    private lateinit var tracks: Map<Region, List<Track>>

    init {
        load()
    }

    fun load() {
        tracks = load(files.load(path))
    }

    operator fun get(region: Region): List<Track> {
        return tracks[region] ?: emptyList()
    }

    private fun load(data: Map<String, Map<String, Any>>): Map<Region, List<Track>> {
        val map = mutableMapOf<Region, MutableList<Track>>()
        var count = 0
        val time = System.currentTimeMillis()
        for ((_, m) in data) {
            val index = m["index"] as Int
            val areas = (m["areas"] as List<Map<String, List<Int>>>).map {
                if (it.containsKey("region")) {
                    val plane = it["plane"] as? Int ?: -1
                    val region = Region(it["region"] as Int)
                    if (plane != -1) {
                        RegionPlane(region.x, region.y, plane)
                    } else {
                        region
                    }
                } else {
                    val x = (it["x"] as List<Int>).toIntArray()
                    val y = (it["y"] as List<Int>).toIntArray()
                    val plane = it["plane"] as? Int ?: 0
                    if (x.size <= 2) {
                        Rectangle(x.first(), y.first(), x.last(), y.last(), plane)
                    } else {
                        Polygon(x, y, plane)
                    }
                }
            }
            for (area in areas) {
                val track = Track(index, area)
                for (region in area.regions) {
                    val tracks = map.getOrPut(region) { mutableListOf() }
                    tracks.add(track)
                    // Prioritise shape checks over region checks
                    tracks.sortBy { it.area is Region }
                }
            }
            count++
        }
        println("Loaded $count ${"music track".plural(count)} in ${System.currentTimeMillis() - time}ms")
        return map
    }

    data class Track(val index: Int, val area: Area)
}