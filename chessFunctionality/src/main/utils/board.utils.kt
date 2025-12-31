package utils

import chessData.EPieceType
import kotlin.code
import kotlin.math.abs

val omniDirectional = arrayOf(7, 8, 9, -1, 1, -9, -8, -7)
val omniDiagonal = arrayOf(7, 9, -7, -9)
val knightPattern = arrayOf(6, 15, 17, 10, -10, -17, -15, -6)
val cardinal = arrayOf(1, 8, -1, -8)

fun isWithinRanks(index: Int, lowerRank: Int, upperRank: Int): Boolean {
    return (index/8 + 1) in lowerRank..upperRank
}

fun isWithinBoard(index: Int): Boolean {
    return index in 0..63
}

fun isFile(file: Char, index: Int): Boolean {
    val intFile = file.lowercaseChar().code - 97
    return intFile == index%8
}

fun isRank(rank: Int, index: Int): Boolean {
    return index/8 + 1 == rank
}

fun getBoardIndices(board: ULong): MutableList<Int> {
    val indices: MutableList<Int> = mutableListOf()

    for(i in 0..63){
        if(getBit(board, i)){
            indices.add(i)
        }
    }
    return indices
}

fun isWhite(piece: EPieceType): Boolean {
    return piece.ordinal in 0..5
}

fun willFileOverflow(current: Int, next: Int): Boolean {
    val currFile = current%8
    val nextFile = next%8
    val dist = abs(currFile - nextFile)

     return dist > 2
}