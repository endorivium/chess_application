package utils

import kotlin.math.round

val empty: ULong = 0x0u
val universe: ULong = 0xffffffffffffffffu

/*returns the bit value at the given index from the given board*/
fun getBit(b: ULong, bitIndex: Int): Boolean {
    //find how much bit must be shifted, so that looked at bit is least significant
    val shift: Int = 63 - bitIndex
    var bitLookup: ULong = b
    //shift it right (max index 31)
    bitLookup = bitLookup shr shift.coerceIn(0..31)

    //if shift is > 31, then it shifts the remaining indices left
    if (shift > 31) {
        val secondShift: Int = shift - 31
        bitLookup = (bitLookup shr secondShift)
    }

    val masked = (bitLookup and 1.toULong())
    return masked.toInt() != 0
}

/*flips the bit value in the given bitboard at the given indices and returns the new board*/
fun flipBits(b: ULong = empty, indices: Array<Int>): ULong {
    var board = b
    for (index in indices) {
        board = flipBit(board, index)
    }
    return board
}

/*flips the bit value in the bitboard at the given index and returns the new board*/
fun flipBit(b: ULong = empty, bitIndex: Int): ULong {
    var board: ULong = b
    val longBitMask = makeLongBitMask(bitIndex)
    board = board xor longBitMask  // Turn bit B on
    return board
}

/*swaps two bits in a bitboard*/
fun swapBit(b: ULong, bitIndex: Int, targetIndex: Int): ULong {
    var board: ULong = b
    var longBitMask = makeLongBitMask(bitIndex).inv()
    board = board and longBitMask // Turn bit A off

    longBitMask = makeLongBitMask(targetIndex)
    board = board or longBitMask  // Turn bit B on
    return board
}

/*created a 64 bitmask with the bit turned on at the given index*/
fun makeLongBitMask(bitIndex: Int): ULong {
    val shift: Int = 63 - bitIndex
    var bit = (1 shl shift.coerceIn(0..31)).toULong()

    //if shift is > 31, then it shifts the remaining indices left
    if (shift >= 31) {
        bit = correctULongConversion(bit)
        val secondShift: Int = shift - 31
        bit = bit shl secondShift
    }
    return bit
}

/*
conversion from Int to ULong fills the 32 most significant bits that were added with the sign bit, leading to wrong
calculations within the bitboard
this function fixes this by cancelling out the added sign bits
 */
fun correctULongConversion(bit: ULong): ULong {
    val bitCorrection = bit shl 1
    val correctedBit = bit xor bitCorrection
    return correctedBit
}

/*for debugging, prints the given bitboard as a readable 8x8 board*/
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