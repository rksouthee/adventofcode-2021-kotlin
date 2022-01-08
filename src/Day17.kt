import kotlin.math.min

fun main() {
    fun triangularNumber(n: Int): Int = (n * (n + 1)) / 2

    val regex = "target area: x=(\\d+)..(\\d+), y=(-\\d+)..(-\\d+)".toRegex()

    fun part1(input: String): Int {
        val (_, _, yMin, _) = regex.find(input)!!.destructured.toList().map { it.toInt() }
        return triangularNumber(-yMin - 1)
    }

    fun hitsMarker(xTarget: Int, xVel: Int, steps: Int): Boolean {
        val n = min(xVel, steps)
        val t = triangularNumber(n - 1)
        return n * xVel - t == xTarget
    }

    fun addHits(xTarget: Int, steps: Int, yVel: Int, velocities: MutableSet<Pair<Int, Int>>) {
        for (xVel in 1..xTarget) {
            if (hitsMarker(xTarget, xVel, steps)) {
                velocities.add(Pair(xVel, yVel))
            }
        }
    }

    fun part2(input: String): Int {
        val velocities = mutableSetOf<Pair<Int, Int>>()
        val (xMin, xMax, yMin, yMax) = regex.find(input)!!.destructured.toList().map { it.toInt() }
        for (yVel in yMin..-yMin) {
            var y = 0
            var step = 0
            while (y >= yMin) {
                if (y in yMin..yMax) {
                    for (x in xMin..xMax) {
                        addHits(x, step, yVel, velocities)
                    }
                }
                y += yVel - step
                step += 1
            }
        }
        return velocities.size
    }

    val testInput = readInput("Day17_test").first()
    val input = readInput("Day17").first()

    check(part1(testInput) == 45)
    println(part1(input))

    check(part2(testInput) == 112)
    println(part2(input))
}