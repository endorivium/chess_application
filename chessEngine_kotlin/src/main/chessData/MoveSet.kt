package chessData

import utils.empty

data class MoveSet(var move: ULong, var attack: ULong, var castle: ULong = empty)