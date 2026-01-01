package utils

import kotlin.math.round

//takes a ULong (64 bits) and a bitIndex and returns whether the bit at the given index is 0 or 1

val empty: ULong = 0x0u
val universe: ULong = 0xffffffffffffffffu
fun getBit(b: ULong, bitIndex: Int): Boolean {
    //find how much bit must be shifted, so that looked at bit is least significant
    val shift: Int = 63 - bitIndex
    var bitLookup: ULong = b
    //shift it right (max 31)
    bitLookup = bitLookup shr shift.coerceIn(0..31)

    //if shift is > 31, then it shifts the remaining indices left
    if (shift > 31) {
        val secondShift: Int = shift - 31
        bitLookup = (bitLookup shr secondShift)
    }

    val masked = (bitLookup and 1.toULong())
    return masked.toInt() != 0
}

fun flipBits(b: ULong = empty, indices: Array<Int>): ULong {
    var board = b
    for (index in indices) {
        board = flipBit(board, index)
    }
    return board
}

fun flipBit(b: ULong = empty, bitIndex: Int): ULong {
    var board: ULong = b
    val longBitMask = makeLongBitMask(bitIndex)
    board = board xor longBitMask  // Turn bit B on
    //printBitDebug( longBitMask, "bit mask:") //debug
    //printBitDebug(board, "set bit:") //debug
    return board
}

fun swapBit(b: ULong, bitIndex: Int, targetIndex: Int): ULong {
    var board: ULong = b
    var longBitMask = makeLongBitMask(bitIndex).inv()
    board = board and longBitMask // Turn bit A off
    //printBitDebug(longBitMask, "bit mask:")
    //printBitDebug(board, "flipped bit:")

    longBitMask = makeLongBitMask(targetIndex)
    board = board or longBitMask  // Turn bit B on
    //printBitDebug(longBitMask, "bit mask:") //debug
    //printBitDebug(board, "swapped:") //debug
    return board
}

fun makeLongBitMask(bitIndex: Int): ULong {
    val shift: Int = 63 - bitIndex
    var bit = (1 shl shift.coerceIn(0..31)).toULong()
    //printBitDebug(bit, "corrected:")
    //printBitDebug(bit, "first shift:")

    //if shift is > 31, then it shifts the remaining indices left
    if (shift >= 31) {
        bit = correctULongConversion(bit)
        val secondShift: Int = shift - 31
        bit = bit shl secondShift
    }
    //printBitDebug(bit, "long bit mask:")
    return bit
}

/*
conversion from Int to ULong fils the 32 most significant bits that were added with the sign bit leading to wrong
calculations within the bitboard
this function fixes this by cancelling out the added sign bits
 */
fun correctULongConversion(bit: ULong): ULong {
    val bitCorrection = bit shl 1
    val correctedBit = bit xor bitCorrection
    //printBitDebug(bitCorrection, "bitCorrection: ")
    return correctedBit
}

fun printBitDebug(bit: ULong, prefixTxt: String = "Initial Value:") {
    val numIndentation = round(40f / prefixTxt.length)
    var indentation = ""
    for (i in 1..numIndentation.toInt()) {
        indentation += "\t"
    }

    val bitText = bit.toString(2).padStart(64, '0')

    println(prefixTxt + indentation)
    for (i in bitText.indices) {
        if (i == 0 || i % 8 == 0)
            println(bitText.substring(i, i + 8))
    }
}