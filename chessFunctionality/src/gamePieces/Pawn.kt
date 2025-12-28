package gamePieces

import chess.utils.empty
import chess.utils.printBitDebug
import chess.utils.flipBit
import gameState.ChessMove
import gameState.GameManager
import kotlin.math.abs

class Pawn(gm: GameManager, piece: EPieceType): ChessPiece(gm ,piece) {

    override fun getPossibleMoves(posIndex: Int): MoveSet {
        val push = getPush(posIndex)
        val attack = getAttack(posIndex)

        //gets pushes that are actually possible by combining (xor) and then excluding (and)
        val possibleMoves: ULong = (push xor gm.bSManager.getBoardState()) and push
        //printBitDebug(possibleMoves, "possibleMoves: ")
        return MoveSet(possibleMoves, attack)
    }

    fun getPush(posIndex: Int): ULong {
        val singlePush = pushSingle(posIndex)
        val doublePush = pushDouble(posIndex)
        val boardState = gm.bSManager.getBoardState()
        var forwardMoves = singlePush xor doublePush
        forwardMoves = (forwardMoves xor boardState) and forwardMoves

        return forwardMoves
    }

    fun getAttack(posIndex: Int): ULong {
        var leftAttack: ULong = empty
        if(posIndex%8 != 0 && posIndex in 8..55){
            leftAttack = flipBit(bitIndex = posIndex + mod*7)
        }
        leftAttack = leftAttack and gm.bSManager.getEnemyBoard(piece)

        var rightAttack: ULong = empty
        if(posIndex%7 != 0 && posIndex in 8..55){
            rightAttack = flipBit(bitIndex = posIndex + mod*9)
        }
        rightAttack = rightAttack and gm.bSManager.getEnemyBoard(piece)

        val attack: ULong = leftAttack xor rightAttack xor enPassantMove(posIndex)
        //printBitDebug(attack, "attack: ")
        return attack
    }

    fun leftEnPassant(posIndex: Int, prevMove: ChessMove): ULong {
        val stepDist = abs(prevMove.initialIndex/8 - prevMove.targetIndex/8)
        val enPassantIndexLeft = if(posIndex%8 != 0) posIndex - 1 else -1
        if(stepDist != 2  || enPassantIndexLeft == -1) return empty

        if(prevMove.chessPiece == getEnemyPawn()
            && prevMove.targetIndex == enPassantIndexLeft) {
            return flipBit(bitIndex = enPassantIndexLeft)
        }
        return empty
    }

    fun rightEnPassant(posIndex: Int, prevMove: ChessMove): ULong {
        val stepDist = abs(prevMove.initialIndex/8 - prevMove.targetIndex/8)
        val enPassantIndexRight = if(posIndex%7 != 0) posIndex + 1 else -1
        if(stepDist != 2 || enPassantIndexRight == -1) return empty

        if(prevMove.chessPiece == getEnemyPawn()
            && prevMove.targetIndex == enPassantIndexRight) {
            return flipBit(bitIndex = enPassantIndexRight)
        }
        return empty
    }

    //TODO: enPassant should only work if pawn piece was moves in previous move by enemy
    fun enPassantMove(posIndex: Int): ULong {
        val prevMove = gm.getPrevMove()
        if(!prevMove.first) return empty
        return leftEnPassant(posIndex, prevMove.second) xor rightEnPassant(posIndex, prevMove.second)
    }

    fun getEnemyPawn(): EPieceType {
        return if(piece == EPieceType.BPawn) EPieceType.WPawn else EPieceType.BPawn
    }

    fun pushSingle(posIndex: Int): ULong{
        var singlePush: ULong = empty
        if(isMoveWithinBoard(posIndex)){
            singlePush = singlePush xor flipBit(singlePush, posIndex + mod*8)
        }
        //printBitDebug(singlePush, "pushSingle: ")
        return singlePush
    }

    fun pushDouble(posIndex: Int): ULong{
        var doublePush: ULong = empty
        if(posIndex in 8..15 || posIndex in 48..55){
            doublePush = flipBit(doublePush, posIndex + mod*16)
        }
        //printBitDebug(doublePush, "pushDouble: ")
        return doublePush
    }

    fun isMoveWithinBoard(posIndex: Int): Boolean {
        return posIndex in 8..55
    }
}