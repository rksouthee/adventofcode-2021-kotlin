fun main() {
    fun solve(input: List<Int>, offset: Int): Int {
        var countIncreasing = 0
        for (i in offset until input.size) {
            if (input[i] > input[i - offset]) {
                ++countIncreasing
            }
        }
        return countIncreasing
    }

    fun part1(input: List<Int>): Int {
        return solve(input, 1)
    }

    fun part2(input: List<Int>): Int {
        return solve(input, 3)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test").map { it.toInt() }
    check(part1(testInput) == 7)

    val input = readInput("Day01").map{ it.toInt() }
    println(part1(input))
    println(part2(input))
}
