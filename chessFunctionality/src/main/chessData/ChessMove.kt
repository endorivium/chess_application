package main.chessData

import main.utils.toIndex

/*
chessPiece should only be used to record chess moves
 */
data class ChessMove(
    val initialCoord: String = "",
    val targetCoord: String = "",
    var initialIndex: Int = -1,
    var targetIndex: Int = -1,
    var chessPiece: EPieceType? = null) {
    init{
        if(!initialCoord.isEmpty()) {
            initialIndex = toIndex(initialCoord)
        }
        if(!targetCoord.isEmpty()){
            targetIndex = toIndex(targetCoord)
        }
    }

    fun assignChessPiece(piece: EPieceType?) {
        chessPiece = piece
    }
}