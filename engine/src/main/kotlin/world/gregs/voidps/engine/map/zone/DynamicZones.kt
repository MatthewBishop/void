package world.gregs.voidps.engine.map.zone

import it.unimi.dsi.fastutil.ints.Int2IntArrayMap
import it.unimi.dsi.fastutil.ints.IntOpenHashSet
import world.gregs.voidps.engine.data.definition.MapDefinitions
import world.gregs.voidps.engine.entity.World
import world.gregs.voidps.engine.entity.obj.GameObjects
import world.gregs.voidps.engine.map.collision.Collisions
import world.gregs.voidps.engine.map.collision.clear
import world.gregs.voidps.type.MapSquareKey
import world.gregs.voidps.type.ZoneKey
import java.util.*
import kotlin.collections.set

class DynamicZones(
    private val objects: GameObjects,
    private val collisions: Collisions,
    private val extract: MapDefinitions
) {
    private val zones: MutableMap<Int, Int> = Int2IntArrayMap()
    private val regions = IntOpenHashSet()

    fun isDynamic(region: MapSquareKey) = regions.contains(region.id)

    fun getDynamicZone(zone: ZoneKey) = zones[zone.id]

    /**
     * @param from The zone to be copied
     * @param to The zone things will be copied to
     */
    fun copy(from: ZoneKey, to: ZoneKey = from, rotation: Int = 0) {
        zones[to.id] = from.rotatedId(rotation)
        update(from, to, rotation, true)
    }

    /**
     * @param from The region to be copied
     * @param to The region to be replaced
     */
    fun copy(from: MapSquareKey, to: MapSquareKey) {
        val targetZones = LinkedList(to.toCuboid().toZones())
        for (zone in from.toCuboid().toZones()) {
            copy(zone, targetZones.poll())
        }
    }

    /**
     * Clear the dynamic [zone] and replace it with the original
     */
    fun clear(zone: ZoneKey) {
        zones.remove(zone.id)
        update(zone, zone, 0, false)
    }

    /**
     * Clear the dynamic [region] and replace it with the original
     */
    fun clear(region: MapSquareKey) {
        for (zone in region.toCuboid().toZones()) {
            clear(zone)
        }
    }

    private fun update(from: ZoneKey, to: ZoneKey, rotation: Int, set: Boolean) {
        objects.reset(to)
        collisions.clear(to)
        extract.loadZone(from, to, rotation)
        for (region in to.toCuboid(radius = 3).toRegions()) {
            if (set) {
                regions.add(region.id)
            } else if (region.toRectangle().toZones().none { zones.containsKey(it.id) }) {
                regions.remove(region.id)
            }
        }
        World.emit(ReloadZone(to))
    }

    companion object {

        fun ZoneKey.dynamicId() =
            toZonePosition(x, y, level)

        fun ZoneKey.rotatedId(rotation: Int) =
            toRotatedZonePosition(
                x,
                y,
                level,
                rotation
            )

        fun getZone(id: Int) = ZoneKey(x(id), y(id), level(id))

        private fun x(id: Int) = id shr 14 and 0x7ff
        private fun y(id: Int) = id shr 3 and 0x7ff
        private fun level(id: Int) = id shr 28 and 0x7ff

        private fun toZonePosition(zoneX: Int, zoneY: Int, level: Int): Int {
            return zoneY + (zoneX shl 14) + (level shl 28)
        }

        private fun toRotatedZonePosition(zoneX: Int, zoneY: Int, level: Int, rotation: Int): Int {
            return rotation shl 1 or (level shl 24) or (zoneX shl 14) or (zoneY shl 3)
        }
    }
}