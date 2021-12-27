data class LineSegment(val x1: Int, val y1: Int, val x2: Int, val y2: Int) {
    val isHorizontal: Boolean
        get() = this.y1 == this.y2

    val isVertical: Boolean
        get() = this.x1 == this.x2

    val width: Int
        get() = kotlin.math.abs(this.x1 - this.x2)

    val height: Int
        get() = kotlin.math.abs(this.y1 - this.y2)
}

fun main() {
    fun countOverlappingPoints(input: List<LineSegment>): Int {
        val points = mutableMapOf<Pair<Int, Int>, Int>()
        for (lineSegment in input) {
            if (lineSegment.isVertical) {
                val x = lineSegment.x1
                if (lineSegment.y1 < lineSegment.y2) {
                    for (y in lineSegment.y1..lineSegment.y2) {
                        points[Pair(x, y)] = points.getOrDefault(Pair(x, y), 0) + 1
                    }
                } else {
                    for (y in lineSegment.y1 downTo lineSegment.y2) {
                        points[Pair(x, y)] = points.getOrDefault(Pair(x, y), 0) + 1
                    }
                }
            } else if (lineSegment.isHorizontal) {
                val y = lineSegment.y1
                if (lineSegment.x1 < lineSegment.x2) {
                    for (x in lineSegment.x1..lineSegment.x2) {
                        points[Pair(x, y)] = points.getOrDefault(Pair(x, y), 0) + 1
                    }
                } else {
                    for (x in lineSegment.x1 downTo lineSegment.x2) {
                        points[Pair(x, y)] = points.getOrDefault(Pair(x, y), 0) + 1
                    }
                }
            } else {
                check(lineSegment.width == lineSegment.height)
                if (lineSegment.x1 < lineSegment.x2) {
                    if (lineSegment.y1 < lineSegment.y2) {
                        for (i in 0..lineSegment.width) {
                            val point = Pair(lineSegment.x1 + i, lineSegment.y1 + i)
                            points[point] = points.getOrDefault(point, 0) + 1
                        }
                    } else {
                        for (i in 0..lineSegment.width) {
                            val point = Pair(lineSegment.x1 + i, lineSegment.y1 - i)
                            points[point] = points.getOrDefault(point, 0) + 1
                        }
                    }
                } else {
                    if (lineSegment.y1 < lineSegment.y2) {
                        for (i in 0 .. lineSegment.width) {
                            val point = Pair(lineSegment.x1 - i, lineSegment.y1 + i)
                            points[point] = points.getOrDefault(point, 0) + 1
                        }
                    } else {
                        for (i in 0 .. lineSegment.width) {
                            val point = Pair(lineSegment.x1 - i, lineSegment.y1 - i)
                            points[point] = points.getOrDefault(point, 0) + 1
                        }
                    }
                }
            }
        }

        return points.count { it.value > 1 }
    }

    fun part1(input: List<LineSegment>): Int {
        return countOverlappingPoints(input.filter{it.isVertical or it.isHorizontal})
    }

    fun part2(input: List<LineSegment>): Int {
        return countOverlappingPoints(input)
    }

    val regex = """(\d+),(\d+) -> (\d+),(\d+)""".toRegex()
    val testInput = readInput("Day05_test")
    val testLineSegments = testInput.map{
        val match = regex.find(it.trim())!!
        val list = match.destructured.toList().map{ value -> value.toInt() }
        LineSegment(list[0], list[1], list[2], list[3])
    }
    check(part1(testLineSegments) == 5)
    check(part2(testLineSegments) == 12)

    val input = readInput("Day05")
    val lineSegments = input.map{
        val match = regex.find(it.trim())!!
        val list = match.destructured.toList().map{ value -> value.toInt() }
        LineSegment(list[0], list[1], list[2], list[3])
    }
    println(part1(lineSegments))
    println(part2(lineSegments))
}