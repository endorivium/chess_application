package chess_piece

/*
* This class describes how many steps a piece may move in the given direction.
* @forwardBound=true pieces may only move towards the enemy's side of the board.
* @orderBound=true pieces can only take steps first in the cardinal, then diagonal direction
*/
class PieceMovement (
    val cardinalSteps: Int = 0,
    val diagonalSteps: Int = 0,
    val orderBound: Boolean = false,
    val forwardBound: Boolean = false
)