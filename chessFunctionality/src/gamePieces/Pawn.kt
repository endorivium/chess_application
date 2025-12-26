package gamePieces

import bitoperation.utils.empty
import bitoperation.utils.getBit
import bitoperation.utils.setBit
import gameState.ChessMove

class Pawn: ChessPiece() {
    override fun getPossibleMoves(posIndex: Int): ULong {
        return getPush(posIndex) xor getAttack(posIndex)
    }

    fun getPush(posIndex: Int): ULong {
        val singlePush = pushSingle(posIndex, EPieceType.wPawn)
        val doublePush = pushDouble(posIndex, EPieceType.wPawn)
        val boardState = boardStateManager.getBoardState()
        var forwardMoves = singlePush xor doublePush
        forwardMoves = (forwardMoves xor boardState) and forwardMoves

        return forwardMoves
    }

    fun getAttack(posIndex: Int): ULong {
        var leftAttack: ULong = empty
        if(posIndex%8 != 0 && posIndex <= 55){
            leftAttack = setBit(bitIndex = posIndex + 7)
        }

        var rightAttack: ULong = empty
        if(posIndex%7 != 0 && posIndex <= 55){
            rightAttack = setBit(bitIndex = posIndex + 9)
        }

        val attack: ULong = leftAttack xor rightAttack xor enPassantMove(posIndex)
        return attack
    }

    fun enPassantMove(posIndex: Int): ULong {
        val boardState = boardStateManager.getBoardState()
        val enPassantIndexLeft = if(posIndex%8 != 0) posIndex - 1 else -1
        val enPassantIndexRight = if(posIndex%7 != 0) posIndex + 1 else -1

        var enPassant: ULong = 0x0u
        if(enPassantIndexLeft != -1 && getBit(boardState, enPassantIndexLeft)) {
            enPassant = setBit(bitIndex = enPassantIndexLeft)
        }
        if(enPassantIndexRight != -1 && getBit(boardState, enPassantIndexRight)) {
            enPassant = enPassant xor setBit(bitIndex = enPassantIndexRight)
        }
        return enPassant
    }

    fun pushSingle(posIndex: Int, piece: EPieceType): ULong{
        //TODO: implement invert for black pieces
        var singlePush: ULong = empty
        val rank = posIndex/8
        if(rank < 8){
            singlePush = singlePush xor setBit(singlePush, posIndex + 8)
        }
        return singlePush
    }

    fun pushDouble(posIndex: Int, piece: EPieceType): ULong{
        //TODO: implement invert for black pieces
        var doublePush: ULong = empty
        val rank = posIndex/8
        if(rank == 1){
            doublePush = setBit(doublePush, posIndex + 16)
        }
        return doublePush
    }

    override fun canExecuteMove(move: ChessMove): Boolean {
        return super.canExecuteMove(move)
    }
}