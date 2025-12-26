package gamePieces

import bitoperation.utils.empty
import gameState.BoardStateManager
import gameState.ChessMove

open class ChessPiece(val boardStateManager: BoardStateManager = BoardStateManager()){
    open fun getPossibleMoves(posIndex: Int): ULong{ return empty }
    open fun canExecuteMove(move: ChessMove): Boolean { return false }
}