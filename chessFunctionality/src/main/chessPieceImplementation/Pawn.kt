package chessPieceImplementation

import utils.empty
import utils.flipBit
import utils.isFile
import utils.isWhite
import utils.isWithinRanks
import chessPieceImplementation.baseImplementation.ChessPiece
import chessData.ChessMove
import chessData.EPieceType
import chessData.MoveSet
import chessStateManagement.BoardStateManager
import chessStateManagement.GameManager
import kotlin.math.abs

class Pawn(val gm: GameManager, bsm: BoardStateManager, piece: EPieceType): ChessPiece(bsm, piece = piece) {

    override fun getPossibleMoves(index: Int): MoveSet {
        val push = getPush(index)
        val attack = getAttack(index)

        //gets pushes that are actually possible by combining (xor) and then excluding (and)
        val possibleMoves: ULong = (push xor bsm.getBoardState()) and push
        //printBitDebug(possibleMoves, "possibleMoves: ")
        return MoveSet(possibleMoves, attack)
    }

    fun getPush(index: Int): ULong {
        val singlePush = pushSingle(index)
        val doublePush = pushDouble(index)
        val boardState = bsm.getBoardState()
        var forwardMoves = singlePush xor doublePush
        forwardMoves = (forwardMoves xor boardState) and forwardMoves

        return forwardMoves
    }

    fun getAttack(index: Int): ULong {
        var leftAttack: ULong = empty
        if(index%8 != 0 && index in 8..55){
            leftAttack = flipBit(bitIndex = index + mod*7)
        }
        leftAttack = leftAttack and bsm.getColorBoard(!isWhite(piece))

        var rightAttack: ULong = empty
        if(index%7 != 0 && index in 8..55){
            rightAttack = flipBit(bitIndex = index + mod*9)
        }
        rightAttack = rightAttack and bsm.getColorBoard(!isWhite(piece))

        val attack: ULong = leftAttack xor rightAttack xor enPassantMove(index)
        //printBitDebug(attack, "attack: ")
        return attack
    }

    fun leftEnPassant(index: Int, prevMove: ChessMove): ULong {
        val stepDist = abs(prevMove.initialIndex/8 - prevMove.targetIndex/8)
        val enPassantIndexLeft = if(!isFile('A', index)) index - 1 else -1
        if(stepDist != 2  || enPassantIndexLeft == -1) return empty

        if(prevMove.chessPiece == getEnemyPawn()
            && prevMove.targetIndex == enPassantIndexLeft) {
            return flipBit(bitIndex = enPassantIndexLeft)
        }
        return empty
    }

    fun rightEnPassant(index: Int, prevMove: ChessMove): ULong {
        val stepDist = abs(prevMove.initialIndex/8 - prevMove.targetIndex/8)
        val enPassantIndexRight = if(!isFile('H', index)) index + 1 else -1
        if(stepDist != 2 || enPassantIndexRight == -1) return empty

        if(prevMove.chessPiece == getEnemyPawn()
            && prevMove.targetIndex == enPassantIndexRight) {
            return flipBit(bitIndex = enPassantIndexRight)
        }
        return empty
    }

    //TODO: enPassant should only work if pawn piece was moves in previous move by enemy
    fun enPassantMove(index: Int): ULong {
        val prevMove = gm.getPrevMove()
        if(!prevMove.first) return empty
        return leftEnPassant(index, prevMove.second) xor rightEnPassant(index, prevMove.second)
    }

    fun getEnemyPawn(): EPieceType {
        return if(piece == EPieceType.BPawn) EPieceType.WPawn else EPieceType.BPawn
    }

    fun pushSingle(index: Int): ULong{
        var singlePush: ULong = empty
        if(isWithinRanks(index, 2, 7)){
            singlePush = singlePush xor flipBit(singlePush, index + mod*8)
        }
        //printBitDebug(singlePush, "pushSingle: ")
        return singlePush
    }

    fun pushDouble(index: Int): ULong{
        var doublePush: ULong = empty
        if(index in 8..15 || index in 48..55){
            doublePush = flipBit(doublePush, index + mod*16)
        }
        //printBitDebug(doublePush, "pushDouble: ")
        return doublePush
    }
}