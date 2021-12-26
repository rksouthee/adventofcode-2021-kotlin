fun main() {
    fun part1(input: List<String>): UInt {
        val numBits = input[0].count()
        var bits = ""

        for (i in 0 until numBits) {
            val onesCount = input.count { it[i] == '1' }
            bits += if (onesCount > input.size - onesCount) '1' else '0'
        }

        val gammaRate = bits.toUInt(2)
        val epsilonRate = gammaRate.inv() and ((1u shl numBits) - 1u)

        return gammaRate * epsilonRate
    }

    fun part2(input: List<String>): UInt {
        var mostCommon = input
        var leastCommon = input
        val numBits = input[0].count()

        // oxygen generator rating
        for (i in 0 until numBits) {
            val pair = mostCommon.partition { it[i] == '1' }
            mostCommon = if (pair.first.size >= pair.second.size) pair.first else pair.second
            if (mostCommon.size == 1) {
                break
            }
        }

        // CO2 scrubber rating
        for (i in 0 until numBits) {
            val pair = leastCommon.partition{ it[i] == '0' }
            leastCommon = if (pair.first.size <= pair.second.size) pair.first else pair.second
            if (leastCommon.size == 1) {
                break
            }
        }

        return leastCommon[0].toUInt(2) * mostCommon[0].toUInt(2)
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 198u)
    check(part2(testInput) == 230u)

    val input = readInput("Day03")
    println(part1(input))
    println(part2(input))
}