package gameState

import bitoperation.utils.empty
import bitoperation.utils.printBitDebug
import gamePieces.EPieceType
import gamePieces.toEnum


class BoardStateManager(
    var boardMap: MutableMap<EPieceType, ULong> = mutableMapOf()
) {
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

    //region BlackBoard
    var wBishopBoard: ULong = 0x2400000000000000u
    var wKingBoard: ULong = 0x800000000000000u
    var wKnightBoard: ULong = 0x4200000000000000u
    var wPawnBoard: ULong = 0xFF000000000000u
    var wQueenBoard: ULong = 0x1000000000000000u
    var wRookBoard: ULong = 0x8100000000000000u
    //endregion

    //region WhiteBoard
    var bBishopBoard: ULong = 0x24u
    var bKingBoard: ULong = 0x8u
    var bKnightBoard: ULong = 0x42u
    var bPawnBoard: ULong = 0xFF00u
    var bQueenBoard: ULong = 0x10u
    var bRookBoard: ULong = 0x81u
    //endregion

    init{
        boardMap = mutableMapOf(
            EPieceType.WBishop to wBishopBoard,
            EPieceType.WKing to wKingBoard,
            EPieceType.WKnight to wKnightBoard,
            EPieceType.WPawn to wPawnBoard,
            EPieceType.WQueen to wQueenBoard,
            EPieceType.WRook to wRookBoard,
            EPieceType.BBishop to bBishopBoard,
            EPieceType.BKing to bKingBoard,
            EPieceType.BKnight to bKnightBoard,
            EPieceType.BPawn to bPawnBoard,
            EPieceType.BQueen to bQueenBoard,
            EPieceType.BRook to bRookBoard
        )
    }

    fun getEnemyPieceBoard(piece: EPieceType): ULong{
        var enemyBoard: ULong = empty
        if(piece.ordinal in 0..5){
            for(i in 6..11){
                enemyBoard = enemyBoard xor getPieceBoard(i.toEnum<EPieceType>()!!)
            }
        }
        else{
            for(i in 0..5){
                enemyBoard = enemyBoard xor getPieceBoard(i.toEnum<EPieceType>()!!)
            }
        }
        printBitDebug(enemyBoard, "enemy board to $piece")
        return enemyBoard
    }

    fun getPieceBoard(piece: EPieceType): ULong{
        //TODO: make sure that board is not null
        return boardMap[piece]!!
    }

    fun getBoardState(): ULong{
        val whiteBoard = wBishopBoard xor wKingBoard xor wKnightBoard xor wPawnBoard xor wQueenBoard xor wRookBoard
        val blackBoard = bBishopBoard xor bKingBoard xor bKnightBoard xor bPawnBoard xor bQueenBoard xor bRookBoard
        return whiteBoard xor blackBoard
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