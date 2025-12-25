package gameState

import kotlin.math.round


class BoardStateManager {
    //TODO: boardstate variable

    companion object {
        //region BOARD_FILES_MASKS
        const val FILE_A: ULong = 0x8080808080808080u
        const val FILE_B: ULong = 0x4040404040404040u
        const val FILE_C: ULong = 0x2020202020202020u
        const val FILE_D: ULong = 0x1010101010101010u
        const val FILE_E: ULong = 0x808080808080808u
        const val FILE_F: ULong = 0x404040404040404u
        const val FILE_G: ULong = 0x202020202020202u
        const val FILE_H: ULong = 0x101010101010101u
        //endregion

        //region BOARD_RANKS_MASKS
        const val RANK_1: ULong = 0xFFu
        const val RANK_2: ULong = 0xFF00u
        const val RANK_3: ULong = 0xFF0000u
        const val RANK_4: ULong = 0xFF000000u
        const val RANK_5: ULong = 0xFF00000000u
        const val RANK_6: ULong = 0xFF0000000000u
        const val RANK_7: ULong = 0xFF000000000000u
        const val RANK_8: ULong = 0xFF00000000000000u
        //endregion
    }

    var


    //takes a ULong (64 bits) and a bitIndex and returns whether the bit at the given index is 0 or 1
    fun getBit(b: ULong, bitIndex: Int): Boolean{
        //find how much bit must be shifted, so that looked at bit is least significant
        val shift: Int = 63 - bitIndex
        var bitLookup: ULong = b
        //shift it right (max 31)
        bitLookup = bitLookup shr shift.coerceIn(0..31)

        //if shift is > 31, then it shifts the remaining indices left
        if(shift > 31){
            val secondShift: Int = shift - 31
            bitLookup = (bitLookup shr secondShift)
        }

        val masked = (bitLookup and 1.toULong())
        return masked.toInt() != 0
    }

    fun swapBit(b: ULong, bitIndex: Int, targetIndex: Int): ULong{
        var board: ULong = b
        var longBitMask = makeLongBitMask(bitIndex).inv()
        board = board and longBitMask // Turn bit A off
        printBitDebug("bit mask:",longBitMask)
        printBitDebug("turned bit off:", board)

        longBitMask = makeLongBitMask(targetIndex)
        board  = board or longBitMask  // Turn bit B on
        printBitDebug("bit mask:", longBitMask) //debug
        printBitDebug("turned bit on (swap):", board) //debug
        return board
    }

    fun makeLongBitMask(bitIndex: Int): ULong{
        val shift: Int = 63 - bitIndex
        var bit = (1 shl shift.coerceIn(0..31)).toULong()
        bit = correctULongConversion(bit)
        printBitDebug("corrected:", bit)
        printBitDebug("first shift:", bit)

        //if shift is > 31, then it shifts the remaining indices left
        if(shift > 31){
            val secondShift: Int = shift - 31
            bit = bit shl secondShift
        }
        printBitDebug("second shift:", bit)
        return bit
    }

    /*
    conversion from Int to ULong fils the 32 most significant bits that were added with the sign bit leading to wrong
    calculations within the bitboard
    this function fixes this by cancelling out the added sign bits
     */
    fun correctULongConversion(bit: ULong): ULong{
        var correctedBit = bit
        val bitCorrection = bit shl 1
        correctedBit = bit xor bitCorrection
        return correctedBit
    }

    fun printBitDebug(prefixTxt: String, bit: ULong){
        val numIndentation = round(40f/ prefixTxt.length)
        var indentation = ""
        for(i in 1..numIndentation.toInt()){
            indentation += "\t"
        }

        println(prefixTxt + indentation + bit.toString(2).padStart(64, '0'))
    }
}

/*fun getBitViaMask(b: ULong, bitIndex: Int): Boolean {
    //shifts bitmask max 31 left
    val shift: Int = 63 - bitIndex
    var bitMask: ULong = (1 shl shift.coerceIn(0..31)).toULong()

    //if shift is > 31, then it shifts the remaining indices left
    if(shift > 31){
        val secondShift: Int = shift - 31
        bitMask = (bitMask shl secondShift).toULong()
    }
    //masks the two
    var masked = (b and bitMask)
    //and then shifts the bit being looked at right, so that it is least significant bit
    masked = masked shr shift
    return masked.toInt() != 0
}*/