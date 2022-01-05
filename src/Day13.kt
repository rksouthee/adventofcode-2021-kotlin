import java.lang.IllegalArgumentException

class Paper(var width: Int, var height: Int) {
    private val values = Array(width * height) { '.' }
    private val stride = width

    fun get(x: Int, y: Int): Char {
        check(x in 0 until width)
        check(y in 0 until height)
        return values[y * stride + x]
    }

    fun set(x: Int, y: Int, c: Char) {
        check(x in 0 until width)
        check(y in 0 until height)
        values[y * stride + x] = c
    }

    private fun foldUp(line: Int) {
        for (i in 0 until height - line - 1) {
            val yAbove = line - i - 1
            val yBelow = line + i + 1
            for (x in 0 until width) {
                if (get(x, yBelow) == '#') {
                    set(x, yAbove, '#')
                }
            }
        }
        height = line
    }

    private fun foldLeft(line: Int) {
        for (i in 0 until width - line - 1) {
            val xLeft = line - i - 1
            val xRight = line + i + 1
            for (y in 0 until height) {
                if (get(xRight, y) == '#') {
                    set(xLeft, y, '#')
                }
            }
        }
        width = line
    }

    fun fold(char: Char, line: Int) {
        if (char == 'y') {
            this.foldUp(line)
        } else {
            check(char == 'x')
            this.foldLeft(line)
        }
    }

    fun countDots(): Int {
        var count = 0
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (get(x, y) == '#') {
                    ++count
                }
            }
        }
        return count
    }
}

fun <T> List<T>.toPair(): Pair<T, T> {
    if (this.size != 2) {
        throw IllegalArgumentException("Expected list to have two elements")
    }
    return Pair(this[0], this[1])
}

fun getInput(name: String): Pair<Paper, List<Pair<Char, Int>>> {
    val input = readInput(name)
    val points = mutableListOf<Pair<Int, Int>>()

    var index = 0
    while (index < input.size && input[index].isNotEmpty()) {
        points.add(input[index].split(',').map{ it.toInt()}.toPair())
        ++index
    }

    var maxWidth = 0
    var maxHeight = 0
    for (point in points) {
        if (point.first >= maxWidth) {
            maxWidth = point.first + 1
        }

        if (point.second >= maxHeight) {
            maxHeight = point.second + 1
        }
    }

    val paper = Paper(maxWidth, maxHeight)
    for (point in points) {
        paper.set(point.first, point.second, '#')
    }

    check(index < input.size && input[index].isEmpty())
    ++index

    val instructions = mutableListOf<Pair<Char, Int>>()
    val regex = "fold along ([xy])=(\\d+)".toRegex()
    while (index < input.size) {
        val (line, value) = regex.find(input[index])!!.destructured
        instructions.add(Pair(line[0], value.toInt()))
        ++index
    }

    return Pair(paper, instructions)
}

fun main() {
    fun part1(paper: Paper, instructions: List<Pair<Char, Int>>): Int {
        paper.fold(instructions[0].first, instructions[0].second)
        return paper.countDots()
    }

    fun part2(paper: Paper, instructions: List<Pair<Char, Int>>) {
        for (i in 1 until instructions.size) {
            paper.fold(instructions[i].first, instructions[i].second)
        }

        for (y in 0 until paper.height) {
            for (x in 0 until paper.width) {
                print(paper.get(x, y))
            }
            println()
        }
    }

    val (testPaper, testInstructions) = getInput("Day13_test")
    val (paper, instructions) = getInput("Day13")

    check(part1(testPaper, testInstructions) == 17)
    println(part1(paper, instructions))
    part2(paper, instructions)
}