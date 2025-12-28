package gamePieces

import chess.utils.empty
import chess.utils.flipBit
import gameState.ChessMove
import gameState.GameManager

open class ChessPiece(
    val gm: GameManager,
    val piece: EPieceType = EPieceType.WPawn,
    var mod: Int = 0){

    init{
        mod = if(piece.ordinal in 0..5) 1 else -1
    }

    /*
    returns possible moves for given chess piece
    first ULong is normal movement, second is attack
    */
    open fun getPossibleMoves(posIndex: Int): MoveSet { return MoveSet(empty, empty) }
    open fun canExecuteMove(move: ChessMove): Pair<Boolean, EMoveType?> {
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