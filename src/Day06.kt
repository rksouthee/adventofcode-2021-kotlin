fun main() {
    fun countLanternfish(ages: List<Int>, days: Int): Long {
        val ageCounts = LongArray(9)
        for (age in ages) {
            ageCounts[age]++
        }

        for (day in 0 until days) {
            val zeroCounts = ageCounts[0]
            for (i in 0 until ageCounts.size - 1) {
                ageCounts[i] = ageCounts[i + 1]
            }
            ageCounts[6] += zeroCounts
            ageCounts[8] = zeroCounts
        }

        return ageCounts.sum()
    }

    fun part1(input: List<Int>): Long {
        return countLanternfish(input, 80)
    }

    fun part2(input: List<Int>): Long {
        return countLanternfish(input, 256)
    }

    val testInput = readInput("Day06_test").first().split(',').map{ it.toInt() }
    check(part1(testInput) == 5934L)
    check(part2(testInput) == 26984457539L)

    val input = readInput("Day06").first().split(',').map{ it.toInt() }
    println(part1(input))
    println(part2(input))
}