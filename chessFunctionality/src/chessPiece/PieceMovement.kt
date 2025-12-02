package chessPiece

/*
* This class describes how many steps a piece may move in the given direction.
* @forwardBound=true pieces may only move towards the enemy's side of the board.
* @orderBound=true pieces can only take steps first in the cardinal, then diagonal direction
* @groundBound=true pieces cannot jump over other pieces
* if @cardinalSteps or @diagonalSteps == -1 then the piece can move unlimited steps in that direction
*/
class PieceMovement (
    val cardinalSteps: Int = 0,
    val diagonalSteps: Int = 0,
    val omnidirectionalSteps: Int = 0,
    val forwardBound: Boolean = false,
    val groundBound: Boolean = true
)