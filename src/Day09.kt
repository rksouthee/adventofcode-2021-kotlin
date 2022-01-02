fun main() {
    fun adjacent(input: List<String>, y: Int, x: Int) = sequence {
        // right
        if (x + 1 < input[y].count()) {
            yield(Pair(y, x + 1))
        }

        // top
        if (y > 0) {
            yield(Pair(y - 1, x))
        }

        // left
        if (x > 0) {
            yield(Pair(y, x - 1))
        }

        // bottom
        if (y + 1 < input.size) {
            yield(Pair(y + 1, x))
        }
    }

    fun isLowPoint(input: List<String>, y: Int, x: Int): Boolean {
        for (neighbour in adjacent(input, y, x)) {
            if (input[neighbour.first][neighbour.second] <= input[y][x]) {
                return false
            }
        }
        return true
    }

    fun lowPoints(input: List<String>) = sequence {
        for (y in 0 until input.size) {
            for (x in 0 until input[y].count()) {
                if (isLowPoint(input, y, x)) {
                    yield(Pair(y, x))
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        var riskLevel = 0
        for (lowPoint in lowPoints(input)) {
            riskLevel += (input[lowPoint.first][lowPoint.second] - '0') + 1
        }
        return riskLevel
    }

    fun getBasinSize(input: List<String>, point: Pair<Int, Int>, visited: MutableSet<Pair<Int, Int>>): Int {
        if (visited.contains(point) || input[point.first][point.second] == '9') {
            return 0
        }

        var basinSize = 1
        visited.add(point)

        for (neighbour in adjacent(input, point.first, point.second)) {
            basinSize += getBasinSize(input, neighbour, visited)
        }

        return basinSize
    }

    fun part2(input: List<String>): Int {
        val basinSizes = mutableListOf<Int>()
        val visited = mutableSetOf<Pair<Int, Int>>()
        for (lowPoint in lowPoints(input)) {
            basinSizes.add(getBasinSize(input, lowPoint, visited))
        }
        basinSizes.sortDescending()
        return basinSizes[0] * basinSizes[1] * basinSizes[2]
    }

    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    check(part1(testInput) == 15)
    println(part1(input))

    check(part2(testInput) == 1134)
    println(part2(input))
}