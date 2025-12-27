package gameState

import bitoperation.utils.empty
import bitoperation.utils.printBitDebug
import bitoperation.utils.setBit
import bitoperation.utils.swapBit
import gamePieces.EPieceType


class BoardStateManager() {
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

    var boards = arrayOf(
        wBishopBoard,
        wKingBoard,
        wKnightBoard,
        wPawnBoard,
        wQueenBoard,
        wRookBoard,
        bBishopBoard,
        bKingBoard,
        bKnightBoard,
        bPawnBoard,
        bQueenBoard,
        bRookBoard
    )

    lateinit var gm: GameManager

    fun setGameManager(gameManager: GameManager){
        gm = gameManager
    }

    fun executeChessMove(move: ChessMove): Boolean{
        val chessPiece: EPieceType = getChessPieceAt(move.initialIndex) ?: return false
        val pieceRule = gm.ruleSet.rules[chessPiece]
            ?: throw IllegalStateException("Chess Piece ($chessPiece) was not found in Rule Set!!")

        val possibleMoves = pieceRule.getPossibleMoves(move.initialIndex)
        val desiredMove = setBit(empty, move.targetIndex)

        if((possibleMoves and desiredMove).countOneBits() != 0){
            boards[chessPiece.ordinal] = swapBit(boards[chessPiece.ordinal], move.initialIndex, move.targetIndex)
            return true
        }
        return false
    }

    fun getChessPieceAt(pieceIndex: Int): EPieceType? {
       val pieceBit = setBit(empty, pieceIndex)

        for(i in boards.indices){
            if((pieceBit and boards[i]).countOneBits() >= 1){
                return EPieceType.fromInt(i)
            }
        }

    return null
    }

    fun getPieceBoard(piece: EPieceType): ULong{
        //TODO: make sure that board is not null
        return boards[piece.ordinal]
    }

    fun getEnemyPieceBoard(piece: EPieceType): ULong{
        var enemyBoard: ULong = empty
        if(piece.ordinal in 0..5){
            for(i in 6..11){
                enemyBoard = enemyBoard xor getPieceBoard(EPieceType.fromInt(i)!!)
            }
        }
        else{
            for(i in 0..5){
                enemyBoard = enemyBoard xor getPieceBoard(EPieceType.fromInt(i)!!)
            }
        }
        printBitDebug(enemyBoard, "enemy board to $piece")
        return enemyBoard
    }

    fun getBoardState(): ULong{
        var board: ULong = empty

        for(piece in boards){
            board = board xor piece
        }
        return board
    }
}