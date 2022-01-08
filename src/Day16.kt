import java.lang.IllegalArgumentException

class BitStream(private val bits: String) {
    var startIndex = 0

    fun read(len: Int): String {
        check(bits.length - startIndex >= len)
        val endIndex = startIndex + len
        val substring = bits.substring(startIndex, endIndex)
        startIndex = endIndex
        return substring
    }
}

var versionSum = 0

fun evaluateLiteral(bitStream: BitStream): Long {
    var literalBits = ""
    var group: String
    do {
        group = bitStream.read(5)
        literalBits += group.drop(1)
    } while (group.first() == '1')
    val padding = bitStream.read(literalBits.length % 4)
    check(padding.all { it == '0' })
    return literalBits.toLong(2)
}

fun evaluateOperator(bitStream: BitStream, typeId: Int): Long {
    val lengthTypeId = bitStream.read(1)
    val values = mutableListOf<Long>()

    if (lengthTypeId == "0") {
        val totalLengthInBits = bitStream.read(15).toInt(2)
        val endIndex = bitStream.startIndex + totalLengthInBits
        while (bitStream.startIndex != endIndex) {
            values.add(evaluatePacket(bitStream))
        }
    } else {
        val numberOfSubPackets = bitStream.read(11).toInt(2)
        for (i in 1..numberOfSubPackets) {
            values.add(evaluatePacket(bitStream))
        }
    }

    return when (typeId) {
        0 -> values.sum()
        1 -> values.reduce { acc, l -> acc * l }
        2 -> values.minOf { it }
        3 -> values.maxOf { it }
        5 -> if (values[0] > values[1]) 1L else 0L
        6 -> if (values[0] < values[1]) 1L else 0L
        7 -> if (values[0] == values[1]) 1L else 0L
        else -> throw IllegalArgumentException("Invalid Type ID")
    }
}

fun evaluatePacket(bitStream: BitStream): Long {
    val version = bitStream.read(3).toInt(2)
    versionSum += version

    val typeId = bitStream.read(3).toInt(2)

    return if (typeId == 4) {
        evaluateLiteral(bitStream)
    } else {
        evaluateOperator(bitStream, typeId)
    }
}

fun main() {
    fun hexToBits(char: Char): String {
        return char.digitToInt(16).toString(2).padStart(4, '0')
    }

    fun part1(input: String): Int {
        versionSum = 0
        val bitStream = BitStream(input.map { hexToBits(it)}.joinToString(""))
        evaluatePacket(bitStream)
        return versionSum
    }

    fun part2(input: String): Long {
        val bitStream = BitStream(input.map{ hexToBits(it)}.joinToString(""))
        return evaluatePacket(bitStream)
    }

    val input = readInput("Day16").first()

    check(part1("8A004A801A8002F478") == 16)
    check(part1("620080001611562C8802118E34") == 12)
    check(part1("C0015000016115A2E0802F182340") == 23)
    check(part1("A0016C880162017C3686B18A3D4780") == 31)
    println(part1(input))

    check(part2("C200B40A82") == 3L)
    check(part2("04005AC33890") == 54L)
    check(part2("880086C3E88112") == 7L)
    check(part2("CE00C43D881120") == 9L)
    check(part2("D8005AC2A8F0") == 1L)
    check(part2("F600BC2D8F") == 0L)
    check(part2("9C005AC2F8F0") == 0L)
    check(part2("9C0141080250320F1802104A08") == 1L)
    println(part2(input))
}