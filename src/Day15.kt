import java.util.*

fun main() {
    fun neighbours(graph: List<List<Int>>, pair: Pair<Int, Int>, repeat: Int) = sequence {
        val size = graph.size * repeat
        val x = pair.first
        val y = pair.second
        if (x + 1 < size) yield(Pair(x + 1, y))
        if (y > 0) yield(Pair(x, y - 1))
        if (x > 0) yield(Pair(x - 1, y))
        if (y + 1 < size) yield(Pair(x, y + 1))
    }

    fun cost(graph: List<List<Int>>, pair: Pair<Int, Int>): Int {
        val x = pair.first
        val y = pair.second
        val xCost = x % graph.size
        val yCost = y % graph.size
        val cost = graph[yCost][xCost]
        val xTile = x / graph.size
        val yTile = y / graph.size
        return (cost + xTile + yTile - 1) % 9 + 1
    }

    fun lowestTotalRisk(graph: List<List<Int>>, repeat: Int): Int {
        val start = Pair(0, 0)
        val goal = Pair(graph.last().size * repeat - 1, graph.size * repeat - 1)

        val compareByCost:  Comparator<Pair<Pair<Int, Int>, Int>> = compareBy { it.second }
        val frontier = PriorityQueue(compareByCost)
        val reached = mutableSetOf<Pair<Int, Int>>()
        val costSoFar = mutableMapOf<Pair<Int, Int>, Int>()

        frontier.add(Pair(start, 0))
        reached.add(start)
        costSoFar[start] = 0

        while (frontier.isNotEmpty()) {
            val current = frontier.remove().first
            if (current == goal) {
                break
            }

            for (neighbour in neighbours(graph, current, repeat)) {
                val newCost = costSoFar[current]!! + cost(graph, neighbour)
                if (!costSoFar.contains(neighbour) || newCost < costSoFar[neighbour]!!) {
                    costSoFar[neighbour] = newCost
                    frontier.add(Pair(neighbour, newCost))
                }
            }
        }

        return costSoFar[goal]!!
    }

    fun part1(input: List<List<Int>>): Int {
        return lowestTotalRisk(input, 1)
    }

    fun part2(input: List<List<Int>>): Int {
        return lowestTotalRisk(input, 5)
    }

    val testInput = readInput("Day15_test")
        .map { line -> line.map { char -> char.digitToInt() }}
    val input = readInput("Day15")
        .map { line -> line.map { char -> char.digitToInt() }}

    check(part1(testInput) == 40)
    println(part1(input))

    check(part2(testInput) == 315)
    println(part2(input))
}