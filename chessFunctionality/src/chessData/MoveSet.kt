package chessData

import chess.utils.empty

data class MoveSet(var move: ULong, var attack: ULong, var rochade: ULong = empty)