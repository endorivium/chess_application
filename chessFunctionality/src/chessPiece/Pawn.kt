package chessPiece

import chessBoard.SquareCoords
import chessBoard.ChessBoardManager
import chessBoard.SpaceOccupation

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
        val targetSquare = if (bFirstMove) SquareCoords(pos.file, pos.rank + 2)
                            else SquareCoords(pos.file, pos.rank + 1)
        var evaluatedSquare: SpaceOccupation
        if (board.isMoveWithinBounds(targetSquare)) {
            evaluatedSquare = board.isSquareOccupied(targetSquare, color)
            if (!evaluatedSquare.isOccupied) {
                availableMoves.add(SquareCoords(targetSquare))
            }
        }

        targetSquare.file++ //diagonal right if enemy piece
        if (board.isMoveWithinBounds(targetSquare)) {
            evaluatedSquare = board.isSquareOccupied(targetSquare, color)
            if (evaluatedSquare.isOccupied
                && evaluatedSquare.isEnemy) {
                availableMoves.add(SquareCoords(targetSquare))
            }
        }

        targetSquare.file -= 2 //diagonal left if enemy piece
        if (board.isMoveWithinBounds(targetSquare)) {
            evaluatedSquare = board.isSquareOccupied(targetSquare, color)
            if (evaluatedSquare.isOccupied
                && evaluatedSquare.isEnemy) {
                availableMoves.add(SquareCoords(targetSquare))
            }
        }
    }
}