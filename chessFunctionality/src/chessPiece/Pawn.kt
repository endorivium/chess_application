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

        var targetSquare = if (bFirstMove) arrayOf(SquareCoords(pos.file, pos.rank + 1), SquareCoords(pos.file, pos.rank + 2))
                            else arrayOf(SquareCoords(pos.file, pos.rank + 1))
        var evaluatedSquare: SpaceOccupation
        if (board.isMoveSequenceWithinBounds(targetSquare)) {
            evaluatedSquare = board.isSquareOccupiedByEnemy(targetSquare[targetSquare.size-1], color)
            if (!evaluatedSquare.isOccupied) {
                availableMoves.add(SquareCoords(targetSquare[targetSquare.size-1]))
            }
        }

        //check diagonal right
        targetSquare = arrayOf(SquareCoords(pos.file + 1, pos.rank + 1))
        if (board.isMoveSequenceWithinBounds(targetSquare)) {
            evaluatedSquare = board.isSquareOccupiedByEnemy(targetSquare[targetSquare.size-1], color)
            if (evaluatedSquare.isOccupied
                && evaluatedSquare.isEnemy) {
                availableMoves.add(SquareCoords(targetSquare[targetSquare.size-1]))
            }
        }

        //check diagonal left
        targetSquare = arrayOf(SquareCoords(pos.file - 1, pos.rank + 1))
        if (board.isMoveSequenceWithinBounds(targetSquare)) {
            evaluatedSquare = board.isSquareOccupiedByEnemy(targetSquare[targetSquare.size-1], color)
            if (evaluatedSquare.isOccupied
                && evaluatedSquare.isEnemy) {
                availableMoves.add(SquareCoords(targetSquare[targetSquare.size-1]))
            }
        }
    }
}