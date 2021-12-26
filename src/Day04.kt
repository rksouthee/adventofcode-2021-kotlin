typealias Board = MutableList<Int>

fun main() {
    fun checkRow(board: Board, row: Int): Boolean {
        for (col in 0 until 5) {
            if (board[row * 5 + col] >= 0) {
                return false
            }
        }
        return true
    }

    fun checkCol(board: Board, col: Int): Boolean {
        for (row in 0 until 5) {
            if (board[row * 5 + col] >= 0) {
                return false
            }
        }
        return true
    }

    fun isBingo(board: Board): Boolean {
        for (row in 0 until 5) {
            if (checkRow(board, row)) {
                return true
            }
        }

        for (col in 0 until 5) {
            if (checkCol(board, col)) {
                return true
            }
        }

        return false
    }

    fun getScores(input: List<String>): List<Int> {
        val lines = input.map{ it.trim() }.filter{ it.isNotEmpty() }
        val numbers = lines[0].split(',').map{ it.toInt() }
        val numBoards = (lines.size - 1) / 5
        var boards = mutableListOf<Board>()

        for (i in 0 until numBoards) {
            val values = mutableListOf<Int>()
            for (j in 0 until 5) {
                values.addAll(lines[i * 5 + j + 1].split("\\s+".toRegex()).map { it.toInt()} )
            }
            check(values.size == 25)
            boards.add(values)
        }

        val scores =  mutableListOf<Int>()
        for (number in numbers) {
            for (board in boards) {
                val index = board.indexOf(number)
                if (index == -1) {
                    continue
                }
                board[index] = -board[index]
            }

            val pair = boards.partition { isBingo(it) }
            boards = pair.second.toMutableList()
            scores.addAll(pair.first.map { board -> board.filter{ it > 0 }.sum() * number })
        }

        return scores.toList()
    }

    fun part1(scores: List<Int>): Int {
        return scores.first()
    }

    fun part2(scores: List<Int>): Int {
        return scores.last()
    }

    val testInput = readInput("Day04_test")
    val testScores = getScores(testInput)
    check(part1(testScores) == 4512)

    val input = readInput("Day04")
    val scores = getScores(input)
    println(part1(scores))
    println(part2(scores))
}