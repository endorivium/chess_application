package chessPieceImplementation.baseImplementation

import chess.utils.empty
import chess.utils.flipBit
import chess.utils.isWhite
import chess.utils.isWithinBoard
import chess.utils.willFileOverflow
import chessData.EMoveType
import chessData.EPieceType
import chessData.MoveSet
import chessData.ChessMove
import chessStateManagement.GameManager

open class ChessPiece(
    val gm: GameManager,
    val piece: EPieceType = EPieceType.WPawn,
    val movePattern: Array<Int> = emptyArray(),
    var mod: Int = 0){

    init{
        mod = if(piece.ordinal in 0..5) 1 else -1
    }

    /*
    returns possible moves for given chess piece
    first ULong is normal movement, second is attack
    */
    open fun getPossibleMoves(index: Int): MoveSet {
        return MoveSet(findMoves(index), findAttacks(index))
    }

    open fun findMoves(index: Int): ULong{
        var moves = empty
        val board = gm.getBSM().getBoardState()

        for(step in movePattern) {
            var next = index
            var move: ULong
            while (isWithinBoard(next + step)) {
                //fixes file overflow
                if (willFileOverflow(next, next + step))
                    break

                next += step
                move = flipBit(empty, next)

                if((move and board).countOneBits() != 0)
                    break

                moves = moves xor move
            }
        }
        return moves
    }

    open fun findAttacks(index: Int): ULong{
        var attacks = empty
        val enemyBoard = gm.getBSM().getColorBoard(!isWhite(piece))
        val allyBoard = gm.getBSM().getColorBoard(isWhite(piece))

        for(step in movePattern){
            var next = index
            var attack: ULong
            var ally: ULong
            while(isWithinBoard(next + step)){
                //fixes file overflow
                if(willFileOverflow(next, next + step))
                    break

                next += step

                attack = flipBit(empty, next)
                ally = attack and allyBoard
                if(ally.countOneBits() != 0)
                    break

                attack = attack and enemyBoard
                if(attack.countOneBits() != 0) {
                    attacks = attacks xor attack
                    break
                }
            }
        }
        return attacks
    }

    fun canExecuteMove(move: ChessMove): Pair<Boolean, EMoveType?> {
        val possibleMoves = getPossibleMoves(move.initialIndex)
        val desiredMove = flipBit(empty, move.targetIndex)

        if((possibleMoves.move and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Push)
        }

        if((possibleMoves.attack and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Attack)
        }

        if((possibleMoves.rochade and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Rochade)
        }

        return Pair(false, null)
    }

    fun isEnemy(other: EPieceType): Boolean {
        return piece.ordinal in 0..5 && other.ordinal !in 0..5
    }
}