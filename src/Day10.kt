fun main() {
    fun syntaxErrorScore(character: Char): Int =
        when (character) {
            ')' -> 3
            ']' -> 57
            '}' -> 1197
            '>' -> 25137
            else -> throw IllegalStateException("Invalid character")
        }

    fun getOpening(character: Char): Char =
        when (character) {
            ')' -> '('
            ']' -> '['
            '}' -> '{'
            '>' -> '<'
            else -> throw IllegalStateException("Invalid character")
        }

    fun syntaxCheck(input: String): Int {
        val stack = mutableListOf<Char>()
        for (character in input) {
            when (character) {
                '(', '[', '{', '<' -> stack.add(character)
                else -> {
                    if (stack.last() != getOpening(character)) {
                        return syntaxErrorScore(character)
                    }
                    stack.removeAt(stack.lastIndex)
                }
            }
        }
        return 0
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { syntaxCheck(it) }
    }

    fun autocompleteScore(character: Char): Int =
        when (character) {
            ')' -> 1
            ']' -> 2
            '}' -> 3
            '>' -> 4
            else -> throw IllegalStateException("Invalid character")
        }

    fun getClosing(character: Char): Char =
        when (character) {
            '(' -> ')'
            '[' -> ']'
            '{' -> '}'
            '<' -> '>'
            else -> throw IllegalStateException("Invalid character")
        }

    fun autocomplete(input: String): Long {
        val stack = mutableListOf<Char>()
        for (character in input) {
            when (character) {
                '(', '[', '{', '<' -> stack.add(character)
                ')', ']', '}', '>' -> stack.removeLast()
                else -> throw IllegalStateException("Invalid character")
            }
        }

        var score = 0L
        while (stack.isNotEmpty()) {
            when (stack.last()) {
                '(', '[', '{', '<' -> {
                    val closing = getClosing(stack.removeLast())
                    score = score * 5 + autocompleteScore(closing)
                }
            }
        }
        return score
    }

    fun part2(input: List<String>): Long {
        val scores = input.filter { syntaxCheck(it) == 0 }.map { autocomplete(it) }.sorted()
        return scores[scores.size / 2]
    }

    val testInput = readInput("Day10_test")
    val input = readInput("Day10")

    check(part1(testInput) == 26397)
    println(part1(input))

    check(part2(testInput) == 288957L)
    println(part2(input))
}