package gameState

import gamePieces.EPieceType

//TODO: add color and piece type
//TODO: initialize indices
class ChessMove(
    val initialCoord: String = "",
    val targetCoord: String = "",
    var initialIndex: Int = -1,
    var targetIndex: Int = -1,
    var chessPiece: EPieceType? = null) {
    init{
        if(!initialCoord.isEmpty() && !targetCoord.isEmpty()) {
            var file = initialCoord[0].code - 97
            var rank = initialCoord[1].code - 49
            initialIndex = rank * 8 + file

            file = targetCoord[0].code - 97
            rank = targetCoord[1].code - 49
            targetIndex = rank * 8 + file
        }
    }
}