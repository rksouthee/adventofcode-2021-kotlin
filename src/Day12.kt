interface Visitor {
    fun visited(node: String): Boolean
    fun enter(node: String)
    fun exit(node: String)
}

fun main() {
    fun readGraph(name: String): Map<String, List<String>> {
        val input = readInput(name)
        val graph = mutableMapOf<String, MutableList<String>>()
        for (line in input) {
            val nodes = line.split('-')
            check(nodes.size == 2)
            graph.getOrPut(nodes.first()) { mutableListOf() }.add(nodes.last())
            graph.getOrPut(nodes.last()) { mutableListOf() }.add(nodes.first())
        }
        return graph
    }

    fun countAllPaths(graph: Map<String, List<String>>, current: String, end: String, visitor: Visitor): Int {
        if (current == end) {
            return 1
        }

        if (visitor.visited(current)) {
            return 0
        }

        visitor.enter(current)
        var pathCount = 0
        for (node in graph[current]!!) {
            pathCount += countAllPaths(graph, node, end, visitor)
        }
        visitor.exit(current)

        return pathCount
    }

    fun part1(graph: Map<String, List<String>>): Int {
        class MyVisitor : Visitor {
            val nodesVisited = mutableSetOf<String>()

            override fun visited(node: String): Boolean {
                return nodesVisited.contains(node) && node.all { it.isLowerCase() }
            }

            override fun enter(node: String) {
                nodesVisited.add(node)
            }

            override fun exit(node: String) {
                nodesVisited.remove(node)
            }
        }

        return countAllPaths(graph, "start", "end", MyVisitor())
    }

    fun part2(graph: Map<String, List<String>>): Int {
        class MyVisitor : Visitor {
            val visitorCount = mutableMapOf<String, Int>()

            override fun visited(node: String): Boolean {
                if (node.all { it.isUpperCase() } || !visitorCount.contains(node)) {
                    return false
                }

                if (visitorCount[node]!! == 1 && node == "start") {
                    return true
                }

                for ((key, value) in visitorCount) {
                    if (key.all { it.isLowerCase() } && value == 2) {
                        return true
                    }
                }

                return false
            }

            override fun enter(node: String) {
                visitorCount.putIfAbsent(node, 0)
                visitorCount[node] = visitorCount[node]!! + 1
            }

            override fun exit(node: String) {
                visitorCount[node] = visitorCount[node]!! - 1
                if (visitorCount[node] == 0) {
                    visitorCount.remove(node)
                }
            }
        }

        return countAllPaths(graph, "start", "end", MyVisitor())
    }

    val testGraph = readGraph("Day12_test")
    val graph = readGraph("Day12")

    check(part1(testGraph) == 226)
    println(part1(graph))

    check(part2(testGraph) == 3509)
    println(part2(graph))
}