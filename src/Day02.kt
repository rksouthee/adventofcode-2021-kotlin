fun main() {
    fun part1(input: List<String>): Int {
        var horizontalPosition = 0
        var depth = 0

        for (line in input) {
            val command = line.split(' ')
            val units = command[1].toInt()
            when (command[0]) {
                "forward" -> horizontalPosition += units
                "down" -> depth += units
                "up" -> depth -= units
            }
        }
        return horizontalPosition * depth
    }

    fun part2(input: List<String>): Int {
        var horizontalPosition = 0
        var depth = 0
        var aim = 0

        for (line in input) {
            val command = line.split(' ')
            val units = command[1].toInt()
            when (command[0]) {
                "down" -> aim += units
                "up" -> aim -=  units
                "forward" -> {
                    horizontalPosition += units
                    depth += aim * units
                }
            }
        }

        return horizontalPosition * depth
    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 150)
    check(part2(testInput) == 900)

    val input = readInput("Day02")
    println(part1(input))
    println(part2(input))
}