package rs.dusk.engine.map.region

import com.github.michaelbull.logging.InlineLogger
import kotlinx.coroutines.*
import org.koin.dsl.module
import rs.dusk.cache.definition.data.MapDefinition
import rs.dusk.cache.definition.decoder.MapDecoder
import rs.dusk.engine.entity.Registered
import rs.dusk.engine.entity.obj.GameObjectFactory
import rs.dusk.engine.entity.obj.Objects
import rs.dusk.engine.event.EventBus
import rs.dusk.engine.map.Tile
import rs.dusk.engine.map.collision.CollisionReader
import kotlin.system.measureTimeMillis

val regionModule = module {
    single { RegionReader(get(), get(), get(), get(), get()) }
    single { MapDecoder(get(), get<Xteas>()) }
}

/**
 * @author Greg Hibberd <greg@greghibberd.com>
 * @since April 16, 2020
 */
class RegionReader(
    private val bus: EventBus,
    private val collisions: CollisionReader,
    private val objects: Objects,
    private val objectFactory: GameObjectFactory,
    private val decoder: MapDecoder
) {

    private val logger = InlineLogger()
    private val scope = CoroutineScope(newSingleThreadContext("RegionReader"))

    val loading = mutableMapOf<Region, Deferred<Boolean>>()

    fun load(region: Region): Boolean = runBlocking {
        loading.getOrPut(region) { loadAsync(region) }.await()
    }

    fun loadAsync(region: Region): Deferred<Boolean> = scope.async {
        val time = measureTimeMillis {
            val def = decoder.getOrNull(region.id) ?: return@async false
            val col = async { collisions.read(region, def) }
            val loc = async { loadObjects(region.tile, def) }
            col.await()
            loc.await()
            bus.emit(RegionLoaded(region))
        }
        logger.info { "Region ${region.id} loaded in ${time}ms" }
        true
    }

    private fun loadObjects(region: Tile, map: MapDefinition) {
        map.objects.forEach { location ->
            // Valid object
            val gameObject = objectFactory.spawn(
                location.id,
                Tile(region.x + location.x, region.y + location.y, location.plane),
                location.type,
                location.rotation
            )
            objects.add(gameObject)
            bus.emit(Registered(gameObject))
        }
    }
}