package com.example.subway.util

import android.content.Context
import org.json.JSONObject
import java.util.LinkedList
import java.util.Queue
import java.util.PriorityQueue

data class SubwayNode(
    val name: String,
    val lines: List<String>,
    val neighbors: List<String>
)

fun loadSubwayGraph(context: Context): Map<String, SubwayNode> {
    return try {
        val jsonStr = context.assets.open("subway_graph_detailed_with_line_nodes.json")
            .bufferedReader().use { it.readText() }

        val jsonObject = JSONObject(jsonStr)
        val graph = mutableMapOf<String, SubwayNode>()

        jsonObject.keys().forEach { stationName ->
            val nodeObject = jsonObject.optJSONObject(stationName) ?: return@forEach
            val linesArray = nodeObject.optJSONArray("lines") ?: return@forEach
            val neighborsArray = nodeObject.optJSONArray("neighbors") ?: return@forEach

            val lines = List(linesArray.length()) { linesArray.getString(it) }
            val neighbors = List(neighborsArray.length()) { neighborsArray.getString(it) }

            val node = SubwayNode(
                name = stationName,
                lines = lines,
                neighbors = neighbors
            )
            graph[stationName] = node
        }

        graph
    } catch (e: Exception) {
        e.printStackTrace()
        emptyMap()
    }
}

data class PathNode(
    val station: String,
    val path: List<Pair<String, String>>, // (역 이름, 호선)
    val transfers: Int,
    val currentLine: String?
)

fun findMinTransferPath(
    graph: Map<String, SubwayNode>,
    start: String,
    goal: String
): List<Pair<String, String>>? {
    val visited = mutableSetOf<Pair<String, String>>() // (역, 노선)
    val queue: Queue<PathNode> = LinkedList()

    graph[start]?.lines?.forEach { line ->
        queue.add(PathNode(start, listOf(Pair(start, line)), 0, line))
    }

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val currStation = current.station

        if (currStation == goal) return current.path

        val node = graph[currStation] ?: continue
        for (neighbor in node.neighbors) {
            val neighborNode = graph[neighbor] ?: continue
            for (line in neighborNode.lines) {
                val visitedKey = Pair(neighbor, line)
                if (visitedKey in visited) continue
                visited.add(visitedKey)

                val transfer = if (current.currentLine != null && current.currentLine != line) 1 else 0
                val newPath = current.path + Pair(neighbor, line)

                queue.add(
                    PathNode(
                        station = neighbor,
                        path = newPath,
                        transfers = current.transfers + transfer,
                        currentLine = line
                    )
                )
            }
        }
    }

    return null
}

data class OptimalPathNode(
    val station: String,
    val path: List<Pair<String, String>>, // (역, 호선)
    val transfers: Int,
    val currentLine: String?
) : Comparable<OptimalPathNode> {
    override fun compareTo(other: OptimalPathNode): Int {
        return when {
            this.transfers != other.transfers -> this.transfers - other.transfers
            else -> this.path.size - other.path.size
        }
    }
}

fun findOptimalTransferPath(
    graph: Map<String, SubwayNode>,
    start: String,
    goal: String
): List<Pair<String, String>>? {
    val visited = mutableSetOf<Pair<String, String>>() // (역, 노선)
    val queue = PriorityQueue<OptimalPathNode>()

    graph[start]?.lines?.forEach { line ->
        queue.add(OptimalPathNode(start, listOf(Pair(start, line)), 0, line))
    }

    while (queue.isNotEmpty()) {
        val current = queue.poll()
        val currStation = current.station

        if (currStation == goal) return current.path

        val node = graph[currStation] ?: continue
        for (neighbor in node.neighbors) {
            val neighborNode = graph[neighbor] ?: continue
            for (line in neighborNode.lines) {
                val visitedKey = Pair(neighbor, line)
                if (visitedKey in visited) continue
                visited.add(visitedKey)

                val transfer = if (current.currentLine != null && current.currentLine != line) 1 else 0
                val newPath = current.path + Pair(neighbor, line)

                queue.add(
                    OptimalPathNode(
                        station = neighbor,
                        path = newPath,
                        transfers = current.transfers + transfer,
                        currentLine = line
                    )
                )
            }
        }
    }

    return null
}
