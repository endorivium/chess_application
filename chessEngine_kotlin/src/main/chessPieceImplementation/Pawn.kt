package chessPieceImplementation

import utils.empty
import utils.flipBit
import utils.isFile
import utils.isWithinRanks
import chessData.ChessMove
import chessData.EMoveType
import chessData.EPieceType
import chessData.MoveSet
import chessPieceImplementation.baseImplementation.SingleStep
import chessStateManagement.BoardStateManager
import utils.isRank
import utils.isWhite
import utils.omniDirectional
import utils.pawnAttackPattern
import kotlin.math.abs

class Pawn(private val bsm: BoardStateManager, piece: EPieceType) : SingleStep(piece = piece, pawnAttackPattern) {

    override fun getPieceMoveSet(index: Int, board: ULong, allyBoard: ULong, enemyBoard: ULong): MoveSet {
        val push = findMoves(index, board)
        val attack = findAttacks(index, allyBoard, enemyBoard)
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
        if (!simulated) {
            if (isRank(1, move.targetIndex) || isRank(8, move.targetIndex)) {
                notifyTransformation()
            }
        }
        return super.canExecuteMove(move, board, allyBoard, enemyBoard, simulated)
    }

    /*returns all squares that the pawn can move to*/
    override fun findMoves(index: Int, board: ULong): ULong {
        val singlePush = pushSingle(index)
        val doublePush = pushDouble(index)
        var forwardMoves = singlePush xor doublePush
        forwardMoves = (forwardMoves xor board) and forwardMoves

        return forwardMoves
    }

    private fun pushSingle(index: Int): ULong {
        var singlePush: ULong = empty
        if (isWithinRanks(index, 2, 7)) {
            singlePush = singlePush xor flipBit(singlePush, index + mod * 8)
        }
        return singlePush
    }

    private fun pushDouble(index: Int): ULong {
        var doublePush: ULong = empty
        if (isRank(2, index) && isWhite(piece)
            || isRank(7, index) && !isWhite(piece)
        ) {
            doublePush = flipBit(doublePush, index + mod * 16)
        }
        return doublePush
    }

    override fun findAttacks(index: Int, allyBoard: ULong, enemyBoard: ULong): ULong {
        var leftAttack: ULong = empty
        if (!isFile('A', index) && isWithinRanks(index, 2, 7)) {
            leftAttack = flipBit(bitIndex = index + mod * 7)
        }
        leftAttack = leftAttack and enemyBoard

        var rightAttack: ULong = empty
        if (!isFile('H', index) && isWithinRanks(index, 2, 7)) {
            rightAttack = flipBit(bitIndex = index + mod * 9)
        }
        rightAttack = rightAttack and enemyBoard

        val attack: ULong = leftAttack xor rightAttack xor enPassantMove(index)
        return attack
    }

    private fun enPassantMove(index: Int): ULong {
        val prevMove = bsm.getPrevMove()
        if (!prevMove.first) return empty
        return leftEnPassant(index, prevMove.second) xor rightEnPassant(index, prevMove.second)
    }

    private fun leftEnPassant(index: Int, prevMove: ChessMove): ULong {
        val stepDist = abs(prevMove.initialIndex / 8 - prevMove.targetIndex / 8)
        val enPassantIndexLeft = if (!isFile('A', index)) index - 1 else -1
        if (stepDist != 2 || enPassantIndexLeft == -1) return empty

        if (prevMove.chessPiece == getEnemyPawn()
            && prevMove.targetIndex == enPassantIndexLeft
        ) {
            return flipBit(bitIndex = enPassantIndexLeft)
        }
        return empty
    }

    private fun rightEnPassant(index: Int, prevMove: ChessMove): ULong {
        val stepDist = abs(prevMove.initialIndex / 8 - prevMove.targetIndex / 8)
        val enPassantIndexRight = if (!isFile('H', index)) index + 1 else -1
        if (stepDist != 2 || enPassantIndexRight == -1) return empty

        if (prevMove.chessPiece == getEnemyPawn()
            && prevMove.targetIndex == enPassantIndexRight
        ) {
            return flipBit(bitIndex = enPassantIndexRight)
        }
        return empty
    }

    private fun getEnemyPawn(): EPieceType {
        return if (piece == EPieceType.BPawn) EPieceType.WPawn else EPieceType.BPawn
    }

    private fun notifyTransformation() {
        bsm.notifyPawnTransformation()
    }
}