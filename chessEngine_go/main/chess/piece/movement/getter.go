package movement

import "chessEngine_go/chess/data"

type Getter interface {
	GetMoveSet(index int, board uint64, allies uint64, enemies uint64) data.MoveSet
}
