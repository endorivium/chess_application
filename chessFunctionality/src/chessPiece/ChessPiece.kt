package chessPiece

import chessBoard.SquareCoords
import chessBoard.ChessBoardManager

open class ChessPiece(
    var type: PType,
    val color: PColor,
    var pos: SquareCoords,
    var board: ChessBoardManager,
    var availableMoves: MutableList<SquareCoords> = emptyList<SquareCoords>().toMutableList(),
    var selected: Boolean = false) {

    fun read(): String{
        val move = pos.convertToString()
        return "$move: $color$type"
    }

    fun readAvailableMoves(): String {
        var moves = ""
        for (i in availableMoves.indices) {
            val moveName = availableMoves[i].convertToString()
            moves += "| $moveName | "
        }
        return moves
    }

    private fun toChar(i: Int) {}

    fun notifySelection(selected: Boolean, recalculate: Boolean = false){
        this.selected = selected
        if(recalculate){
            findAvailableMoves()
        }
    }

    open fun findAvailableMoves(){
        //TODO: override in children
    }
}