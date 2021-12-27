fun main() {
    fun part1(input: List<Int>): Int {
        fun cost(target: Int): Int = input.sumOf { kotlin.math.abs(it - target)}
        val sorted = input.sorted()
        val halfN = input.size / 2
        return if (input.size % 2 == 0) {
            kotlin.math.min(cost(sorted[halfN]), cost(sorted[halfN + 1]))
        } else {
            cost(sorted[halfN])
        }
    }

    fun triangular(n: Int): Int = (n * (n + 1)) / 2

    fun part2(input: List<Int>): Int {
        fun cost(target: Int) = input.sumOf { triangular(kotlin.math.abs(it - target))}
        val sum = input.sumOf { it.toDouble() }
        val mean = sum / input.size.toDouble()
        val floor = kotlin.math.floor(mean).toInt()
        val ceil = kotlin.math.ceil(mean).toInt()
        return kotlin.math.min(cost(floor), cost(ceil))
    }

    val testInput = readInput("Day07_test").first().split(',').map{it.toInt()}
    check(part1(testInput) == 37)
    check(part2(testInput) == 168)

    val input = readInput("Day07").first().split(',').map{it.toInt()}
    println(part1(input))
    println(part2(input))
}