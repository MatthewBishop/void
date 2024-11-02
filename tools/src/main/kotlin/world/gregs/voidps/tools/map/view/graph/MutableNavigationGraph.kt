package world.gregs.voidps.tools.map.view.graph

import world.gregs.voidps.type.CoordGrid
import world.gregs.yaml.Yaml

class MutableNavigationGraph {

    val adjacencyList = mutableMapOf<CoordGrid, MutableList<Link>>()
    var changed = false

    fun addNode(x: Int, y: Int, z: Int): CoordGrid = getNodeOrNull(x, y, z) ?: createNode(x, y, z)

    fun getNodeOrNull(x: Int, y: Int, z: Int) = getNodeOrNull(CoordGrid.id(x, y, z))

    fun getNodeOrNull(id: Int) = adjacencyList.keys.firstOrNull { it.id == id }

    fun contains(tile: CoordGrid) = adjacencyList.contains(tile)

    private fun createNode(x: Int, y: Int, z: Int): CoordGrid {
        val node = CoordGrid(x, y, z)
        adjacencyList.putIfAbsent(node, mutableListOf())
        changed = true
        return node
    }

    fun removeNode(node: CoordGrid) {
        if (adjacencyList.remove(node) != null) {
            changed = true
        }
    }

    fun addLink(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Link = addLink(CoordGrid(x1, y1, z1), CoordGrid(x2, y2, z2))

    fun addLink(start: CoordGrid, end: CoordGrid): Link = getLinkOrNull(start, end) ?: createLink(start, end)

    fun getLinkOrNull(start: CoordGrid, end: CoordGrid): Link? = getLinks(start).firstOrNull { it.end == end }

    fun getLinkOrNull(x1: Int, y1: Int, z1: Int, x2: Int, y2: Int, z2: Int): Link? = getLinkOrNull(CoordGrid(x1, y1, z1), CoordGrid(x2, y2, z2))

    fun getLinks(node: CoordGrid): List<Link> = adjacencyList[node] ?: emptyList()

    fun getLinked(node: CoordGrid): List<Link> {
        return adjacencyList.flatMap { (_, adj) -> adj.filter { link -> link.end == node } }.toList()
    }

    private fun createLink(start: CoordGrid, end: CoordGrid): Link {
        val link = Link(start, end)
        adjacencyList.getOrPut(start) { mutableListOf() }.add(link)
        changed = true
        return link
    }

    fun removeLink(link: Link) {
        if (adjacencyList[link.start]?.remove(link) == true) {
            changed = true
        }
    }

    fun updateNode(original: CoordGrid, x: Int, y: Int, z: Int): CoordGrid {
        val node = createNode(x, y, z)
        if (node == original) {
            return original
        }
        val removed = adjacencyList.remove(original)?.toMutableList() ?: mutableListOf()
        adjacencyList[node] = removed
        removed.forEach {
            it.start = node
        }
        adjacencyList.forEach { (_, adj) ->
            adj.forEach { link ->
                if (link.end == original) {
                    link.end = node
                }
            }
        }
        removeNode(original)
        changed = true
        return node
    }

    companion object {

        private val yaml = Yaml()


        fun save(graph: MutableNavigationGraph, path: String = "./navgraph.json") {
            yaml.save(path, graph.adjacencyList.mapKeys { it.key.id })
        }

        @Suppress("UNCHECKED_CAST")
        fun load(path: String = "./navgraph.json"): MutableNavigationGraph {
            val graph = MutableNavigationGraph()
            val map: Map<String, List<Map<String, Any>>> = yaml.load(path)
            map.forEach { (key, list) ->
                graph.adjacencyList[CoordGrid(key.toInt())] = list.map {
                    Link(CoordGrid(it["start"] as Int),
                        CoordGrid(it["end"] as Int),
                        it["actions"] as? List<String>,
                        it["requirements"] as? List<String>
                    )
                }.toMutableList()
            }
            return graph
        }
    }
}