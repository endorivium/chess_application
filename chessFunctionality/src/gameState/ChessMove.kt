package gameState

import chess.utils.toIndex
import gamePieces.EPieceType

//TODO: add color and piece type
//TODO: initialize indices
data class ChessMove(
    val initialCoord: String = "",
    val targetCoord: String = "",
    var initialIndex: Int = -1,
    var targetIndex: Int = -1,
    var chessPiece: EPieceType? = null) {
    init{
        if(!initialCoord.isEmpty() && !targetCoord.isEmpty()) {
            initialIndex = toIndex(initialCoord)
            targetIndex = toIndex(targetCoord)
        }
    }

    fun assignChessPiece(piece: EPieceType?) {
        chessPiece = piece
    }
}