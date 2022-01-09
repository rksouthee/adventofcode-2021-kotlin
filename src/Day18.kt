@file:Suppress("ControlFlowWithEmptyBody")

class Node(private var parent: Node?, var left: Node?, var right: Node?, var value: Int) {
    init {
        left?.parent = this
        right?.parent = this
    }

    fun getMagnitude(): Int {
        if (this.left != null) {
            check(this.right != null)
            val leftMagnitude = this.left!!.getMagnitude()
            val rightMagnitude = this.right!!.getMagnitude()
            return 3 * leftMagnitude + 2 * rightMagnitude
        }
        return this.value
    }

    private fun toStringRecursive(node: Node, string: String): String {
        var newString = string
        if (node.left != null) {
            check(node.right != null)
            newString += '['
            newString = toStringRecursive(node.left!!, newString)
            newString += ','
            newString = toStringRecursive(node.right!!, newString)
            newString += ']'
        } else {
            newString += node.value.toString()
        }
        return newString
    }

    override fun toString(): String {
        return this.toStringRecursive(this, "")
    }

    private fun isLeftSuccessor(): Boolean = this.parent != null && this.parent!!.left == this
    private fun isRightSuccessor(): Boolean = this.parent != null && this.parent!!.right == this

    private fun getLeafPredecessor(): Node? {
        check(this.left == null && this.right == null)

        var node: Node? = this
        if (node!!.isRightSuccessor()) {
            node = node.parent!!.left
            while (node!!.left != null) node = node.left
            return node
        }

        do node = node!!.parent while (node!!.isLeftSuccessor())
        if (!node.isRightSuccessor()) return null

        node = node.parent!!.left
        while (node?.right != null) node = node.right
        return node
    }

    private fun getLeafSuccessor(): Node? {
        check(this.left == null && this.right == null)

        var node: Node? = this
        if (node!!.isLeftSuccessor()) {
            node = node.parent!!.right
            while (node!!.left != null) node = node.left
            return node
        }

        do node = node!!.parent while (node!!.isRightSuccessor())
        if (!node.isLeftSuccessor()) return null

        node = node.parent!!.right
        while (node?.left != null) node = node.left
        return node
    }

    private fun explode() {
        check(this.left != null && this.right != null)
        val left = this.left!!.getLeafPredecessor()
        if (left != null) left.value += this.left!!.value

        val right = this.right!!.getLeafSuccessor()
        @Suppress("ControlFlowWithEmptyBody")
        if (right != null) right.value += this.right!!.value

        this.left = null
        this.right = null
        this.value = 0
    }

    private fun reduceNestingRecursive(depth: Int): Boolean {
        if (this.left != null) {
            check(this.right != null)
            if (this.left!!.reduceNestingRecursive(depth + 1)) return true
            if (this.right!!.reduceNestingRecursive(depth + 1)) return true
            if (depth >= 4) {
                this.explode()
                return true
            }
        }
        return false
    }

    private fun split() {
        check(this.left == null && this.right == null)
        this.left = Node(this, null, null, this.value / 2)
        this.right = Node(this, null, null, this.value - this.left!!.value)
    }

    private fun reduceLargeRecursive(): Boolean {
        if (this.left != null) {
            check(this.right != null)
            if (this.left!!.reduceLargeRecursive()) return true
            if (this.right!!.reduceLargeRecursive()) return true
        } else {
            if (this.value >= 10) {
                this.split()
                return true
            }
        }
        return false
    }

    private fun reduce() {
        while (this.reduceNestingRecursive(0) || this.reduceLargeRecursive())
            ;
    }

    fun add(other: Node): Node {
        val node = Node(null, this, other, 0)
        node.reduce()
        return node
    }
}

class Parser(private val input: String) {
    private var startIndex = 0

    fun peek(char: Char) = input[startIndex] == char

    fun expect(): Char {
        val char = input[startIndex]
        ++startIndex
        return char
    }

    fun expect(char: Char) {
        check(peek(char))
        expect()
    }
}

fun parsePair(parser: Parser, node: Node) {
    parser.expect('[')

    node.left = Node(node, null, null, 0)
    if (parser.peek('[')) {
        parsePair(parser, node.left!!)
    } else {
        node.left!!.value = parser.expect() - '0'
    }

    parser.expect(',')

    node.right = Node(node, null, null, 0)
    if (parser.peek('[')) {
        parsePair(parser, node.right!!)
    } else {
        node.right!!.value = parser.expect() - '0'
    }

    parser.expect(']')
}

fun String.toNode(): Node {
    val parser = Parser(this)
    val root = Node(null, null, null, 0)
    parsePair(parser, root)
    return root
}

fun main() {
    fun part1(input: List<String>): Int {
        val numbers = input.map { it.toNode() }
        var result = numbers.first()
        for (number in numbers.drop(1)) {
            result = result.add(number)
        }
        return result.getMagnitude()
    }

    fun part2(input: List<String>): Int {
        fun sum(a: String, b: String): Int {
            val aNode = a.toNode()
            val bNode = b.toNode()
            return aNode.add(bNode).getMagnitude()
        }

        var largestMagnitude = 0
        for (i in 0 until input.lastIndex) {
            for (j in i + 1 until input.size) {
                var magnitude = sum(input[i], input[j])
                if (magnitude > largestMagnitude) largestMagnitude = magnitude
                magnitude = sum(input[j], input[i])
                if (magnitude > largestMagnitude) largestMagnitude = magnitude
            }
        }
        return largestMagnitude
    }

    fun testAddReduce() {
        val a = "[[[[4,3],4],4],[7,[[8,4],9]]]".toNode()
        val b = "[1,1]".toNode()
        check(a.add(b).toString() == "[[[[0,7],4],[[7,8],[6,0]]],[8,1]]")
    }

    fun testSum0() {
        val list = listOf(
            "[1,1]",
            "[2,2]",
            "[3,3]",
            "[4,4]"
        ).map { it.toNode() }

        var result = list.first()
        for (number in list.drop(1)) {
            result = result.add(number)
        }

        check(result.toString() == "[[[[1,1],[2,2]],[3,3]],[4,4]]")
    }

    testAddReduce()
    testSum0()

    val testInput = readInput("Day18_test")
    val input = readInput("Day18")

    check(part1(testInput) == 4140)
    println(part1(input))

    check(part2(testInput) == 3993)
    println(part2(input))
}