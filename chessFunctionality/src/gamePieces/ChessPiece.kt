package gamePieces

import chess.utils.empty
import chess.utils.flipBit
import chess.utils.isWithinBoard
import chess.utils.willFileOverflow
import gameState.ChessMove
import gameState.GameManager

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
    open fun getPossibleMoves(posIndex: Int): MoveSet {
        return MoveSet(findMoves(posIndex), findAttacks(posIndex))
    }

    open fun findMoves(posIndex: Int): ULong{
        var moves = empty
        val board = gm.bSManager.getBoardState()

        for(step in movePattern) {
            var next = posIndex
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

    open fun findAttacks(posIndex: Int): ULong{
        var attacks = empty
        val board = gm.bSManager.getBoardState()

        for(step in movePattern){
            var next = posIndex
            var attack: ULong
            while(isWithinBoard(next + step)){
                //fixes file overflow
                if(willFileOverflow(next, next + step))
                    break

                next += step

                attack = flipBit(empty, next)
                attack = attack and board
                if(attack.countOneBits() != 0
                    && isEnemy(gm.bSManager.getPieceAt(next)!!)) {
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

        if((possibleMoves.movement and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Push)
        }

        if((possibleMoves.attack and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Attack)
        }

        return Pair(false, null)
    }

    fun isEnemy(other: EPieceType): Boolean {
        return piece.ordinal in 0..5 && other.ordinal !in 0..5
    }
}