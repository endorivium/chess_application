package chessPiece

import chessBoard.SquareCoords
import chessBoard.ChessBoardManager

class Pawn(
    type: PType,
    color: PColor,
    pos: SquareCoords,
    board: ChessBoardManager,
    var bFirstMove: Boolean = false
) : ChessPiece(type, color, pos, board) {

    override fun findAvailableMoves() {
        if (color == PColor.White)
            return
        val targetSquare = SquareCoords(pos.file, pos.rank + 2) //two steps on first move
        var evaluatedSquare = board.isSquareOccupied(targetSquare)
        if (bFirstMove && !evaluatedSquare.first) {
            availableMoves.add(SquareCoords(targetSquare))
        }

        targetSquare.rank-- //move one forward
        if (board.isMoveWithinBounds(targetSquare)) {
            evaluatedSquare = board.isSquareOccupied(targetSquare)
            if (!evaluatedSquare.first) {
                availableMoves.add(SquareCoords(targetSquare))
            }
        }

        targetSquare.file++ //diagonal right if enemy piece
        if (board.isMoveWithinBounds(targetSquare)) {
            evaluatedSquare = board.isSquareOccupied(targetSquare)
            if (evaluatedSquare.first
                && evaluatedSquare.second != color
                && evaluatedSquare.second != PColor.None
            ) {
                availableMoves.add(SquareCoords(targetSquare))
            }
        }

        targetSquare.file -= 2 //diagonal left if enemy piece
        if (board.isMoveWithinBounds(targetSquare)) {
            evaluatedSquare = board.isSquareOccupied(targetSquare)
            if (evaluatedSquare.first
                && evaluatedSquare.second != color
                && evaluatedSquare.second != PColor.None
            ) {
                availableMoves.add(SquareCoords(targetSquare))
            }
        }
    }
}