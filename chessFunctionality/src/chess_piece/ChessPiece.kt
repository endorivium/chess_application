package chess_piece

import chess_board.BoardCoords

/*
TODO:
- PieceType type
- board position
- special action pre-move
- special action post-move
- check if piece can take other piece
 */
open class ChessPiece(
    var type: PType,
    val color: PColor,
    var boardPos: BoardCoords,
    var availableMoves: MutableList<BoardCoords> = emptyList<BoardCoords>().toMutableList(),
    var selected: Boolean = false,
    var movement: PieceMovement = PieceMovement()) {

    init{
        //TODO: depending on piece type, init its movement
    }

    fun read(): String{
        return "$color$type"
    }

    fun notifySelection(selected: Boolean){
        this.selected = selected
        if(!selected){
            //TODO: recalculate available moves
        }
    }

    fun findAvailableMoves() {
    }
}