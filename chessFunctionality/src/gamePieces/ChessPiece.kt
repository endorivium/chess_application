package gamePieces

import bitoperation.utils.empty
import gameState.ChessMove
import gameState.GameManager

open class ChessPiece(
    val gm: GameManager,
    val piece: EPieceType = EPieceType.WPawn,
    var mod: Int = 0){

    init{
        mod = if(piece.ordinal in 0..5) 1 else -1
    }

    open fun getPossibleMoves(posIndex: Int): ULong{ return empty }
    open fun canExecuteMove(move: ChessMove): Boolean { return false }

    fun isEnemy(other: EPieceType): Boolean {
        return piece.ordinal in 0..5 && other.ordinal !in 0..5
    }
}