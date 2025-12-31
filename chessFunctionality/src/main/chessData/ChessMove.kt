package chessData

import utils.toIndex

/*
chessPiece is only used to record move history and is not assigned until after execution of the move
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