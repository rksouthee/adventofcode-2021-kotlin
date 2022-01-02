fun main() {
    fun countEasyDigits(input: String): Int {
        val (_, output) = input.split(" | ", limit=2)
        return output.split(' ').filter {
            when (it.count()) {
                2, 3, 4, 7 -> true
                else -> false
            }
        }.count()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { countEasyDigits(it) }
    }

    fun decode(input: String): Int {
        val (patterns, output) = input.split(" | ", limit=2)
        val patternsSet = patterns.split(' ').map { it.toSet() }
        val digits = Array<Set<Char>>(10) { setOf<Char>() }
        digits[1] = patternsSet.find { it.size == 2 }!!
        digits[4] = patternsSet.find { it.size == 4 }!!
        digits[7] = patternsSet.find { it.size == 3 }!!
        digits[8] = patternsSet.find { it.size == 7 }!!
        digits[3] = patternsSet.find { it.size == 5 && it.containsAll(digits[1]) }!!
        digits[2] = patternsSet.find { it.size == 5 && it.intersect(digits[4]).size == 2}!!
        digits[5] = patternsSet.find { it.size == 5 && it != digits[3] && it != digits[2] }!!
        digits[6] = patternsSet.find { it.size == 6 && !it.containsAll(digits[1]) }!!
        digits[9] = patternsSet.find { it.size == 6 && it.containsAll(digits[4]) }!!
        digits[0] = patternsSet.find { it.size == 6 && it != digits[6] && it != digits[9] }!!

        var result = 0
        for (digit in output.split(' ')) {
            result = result * 10 + digits.indexOf(digit.toSet())
        }
        return result
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { decode(it) }
    }

    val testInput = readInput("Day08_test")
    check(part1(testInput) == 26)
    check(part2(testInput) == 61229)

    val input = readInput("Day08")
    println(part1(input))
    println(part2(input))
}