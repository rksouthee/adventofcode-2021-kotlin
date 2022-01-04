fun main() {
    fun adjacent(y: Int, x: Int) = sequence {
        if (x < 9) yield(Pair(y, x + 1))
        if (y > 0 && x < 9) yield(Pair(y - 1, x + 1))
        if (y > 0) yield(Pair(y - 1, x))
        if (y > 0 && x > 0) yield(Pair(y - 1, x - 1))
        if (x > 0) yield(Pair(y, x - 1))
        if (y < 9 && x > 0) yield(Pair(y + 1, x - 1))
        if (y < 9) yield(Pair(y + 1, x))
        if (y < 9 && x < 9) yield(Pair(y + 1, x + 1))
    }
    fun increaseEnergyLevel(grid: MutableList<MutableList<Int>>, y: Int, x: Int, flashed: MutableList<MutableList<Boolean>>): Int {
        var flashes = 0

        ++grid[y][x]

        if (grid[y][x] > 9 && !flashed[y][x]) {
            flashed[y][x] = true
            ++flashes

            for (neighbour in adjacent(y, x)) {
                flashes += increaseEnergyLevel(grid, neighbour.first, neighbour.second, flashed)
            }
        }

        return flashes
    }

    /**
     * @return the number of flashes that occurred
     */
    fun step(grid: MutableList<MutableList<Int>>): Int {
        check(grid.size == 10 && grid.first().size == 10)
        val flashed = MutableList(10) { MutableList(10) { false } }
        var flashes = 0

        for (y in 0 until grid.size) {
            for (x in 0 until grid.first().size ) {
                flashes += increaseEnergyLevel(grid, y, x, flashed)
            }
        }

        grid.forEach { it.replaceAll { energyLevel -> if (energyLevel > 9) 0 else energyLevel } }
        return flashes
    }

    fun part1(input: List<List<Int>>): Int {
        val grid = input.map { it.toMutableList() }.toMutableList()
        var flashes = 0
        for (i in 1..100) {
            flashes += step(grid)
        }
        return flashes
    }

    fun part2(input: List<List<Int>>): Int {
        val grid = input.map { it.toMutableList() }.toMutableList()
        var steps = 0

        while (true) {
            step(grid)
            ++steps
            if (grid.all { it.all { value -> value == 0 } })
                break
        }

        return steps
    }

    val testInput = readInput("Day11_test").map { it.map { char -> char.digitToInt() } }
    val input = readInput("Day11").map { it.map { char -> char.digitToInt() } }

    check(part1(testInput) == 1656)
    println(part1(input))

    check(part2(testInput) == 195)
    println(part2(input))
}