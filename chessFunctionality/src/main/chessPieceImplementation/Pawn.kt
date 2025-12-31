package chessPieceImplementation

import utils.empty
import utils.flipBit
import utils.isFile
import utils.isWithinRanks
import chessPieceImplementation.baseImplementation.ChessPiece
import chessData.ChessMove
import chessData.EMoveType
import chessData.EPieceType
import chessData.MoveSet
import chessStateManagement.BoardStateManager
import utils.isRank
import kotlin.math.abs

class Pawn(private val bsm: BoardStateManager, piece: EPieceType): ChessPiece(piece = piece) {

    override fun getPossibleMoves(index: Int, board: ULong, allyBoard: ULong, enemyBoard: ULong): MoveSet {
        val push = getPush(index, board)
        val attack = getAttack(index, enemyBoard)
        val possibleMoves: ULong = (push xor board) and push

        return MoveSet(possibleMoves, attack)
    }



    override fun canExecuteMove(
        move: ChessMove,
        board: ULong,
        allyBoard: ULong,
        enemyBoard: ULong,
        simulated: Boolean
    ): Pair<Boolean, EMoveType?> {
        if(!simulated){
            if(isRank(1, move.targetIndex) || isRank(8, move.targetIndex)){
                notifyTransformation()
            }
        }
        return super.canExecuteMove(move, board, allyBoard, enemyBoard, simulated)
    }

    fun getPush(index: Int, board: ULong): ULong {
        val singlePush = pushSingle(index)
        val doublePush = pushDouble(index)
        var forwardMoves = singlePush xor doublePush
        forwardMoves = (forwardMoves xor board) and forwardMoves

        return forwardMoves
    }

    fun getAttack(index: Int, enemyBoard: ULong): ULong {
        var leftAttack: ULong = empty
        if(index%8 != 0 && index in 8..55){
            leftAttack = flipBit(bitIndex = index + mod*7)
        }
        leftAttack = leftAttack and enemyBoard

        var rightAttack: ULong = empty
        if(index%7 != 0 && index in 8..55){
            rightAttack = flipBit(bitIndex = index + mod*9)
        }
        rightAttack = rightAttack and enemyBoard

        val attack: ULong = leftAttack xor rightAttack xor enPassantMove(index)
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

    fun enPassantMove(index: Int): ULong {
        val prevMove = bsm.getPrevMove()
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

    fun notifyTransformation(){
        bsm.notifyPawnTransformation()
    }
}