fun main() {
    fun step(pairCounts: Map<Pair<Char, Char>, Long>, rules: Map<Pair<Char, Char>, Char>): Map<Pair<Char, Char>, Long> {
        val newPairCounts = mutableMapOf<Pair<Char, Char>, Long>()
        for ((pair, count) in pairCounts) {
            val newChar = rules[pair]!!

            val left = Pair(pair.first, newChar)
            newPairCounts.putIfAbsent(left, 0)
            newPairCounts[left] = count + newPairCounts[left]!!

            val right = Pair(newChar, pair.second)
            newPairCounts.putIfAbsent(right, 0)
            newPairCounts[right] = count + newPairCounts[right]!!
        }

        return newPairCounts
    }

    fun steps(input: List<String>, steps: Int): Long {
        val polymerTemplate = input.first()
        var pairCounts = polymerTemplate
            .zipWithNext()
            .groupingBy{ it }
            .eachCount()
            .mapValues { it.value.toLong() }
        val rules = input.subList(2, input.size)
            .map { it.split(" -> ") }.associate { Pair(it[0][0], it[0][1]) to it[1][0] }

        for (i in 1..steps) {
            pairCounts = step(pairCounts, rules)
        }

        val letterCounts = mutableMapOf<Char, Long>()
        for ((pair, count) in pairCounts) {
            letterCounts[pair.first] = count + letterCounts.getOrDefault(pair.first, 0L)
            letterCounts[pair.second] = count + letterCounts.getOrDefault(pair.second, 0L)
        }
        letterCounts[polymerTemplate.first()] = 1 + letterCounts[polymerTemplate.first()]!!
        letterCounts[polymerTemplate.last()] = 1 + letterCounts[polymerTemplate.last()]!!

        val max = letterCounts.maxByOrNull { it.value }!!.value
        val min = letterCounts.minByOrNull { it.value }!!.value

        return (max - min) / 2
    }

    fun part1(input: List<String>): Long {
        return steps(input, 10)
    }

    fun part2(input: List<String>): Long {
        return steps(input, 40)
    }

    val testInput = readInput("Day14_test")
    val input = readInput("Day14")

    check(part1(testInput) == 1588L)
    println(part1(input))

    check(part2(testInput) == 2188189693529L)
    println(part2(input))
}