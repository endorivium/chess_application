package piece

import (
	"chessEngine_go/chess/data"
)

type Mover interface {
	CanExecuteMove(move data.ChessMove, board uint64, allies uint64, enemies uint64, simulated bool) (bool, MovementType)
	GetMoveSet(index int, board uint64, allies uint64, enemies uint64) data.MoveSet
	FindMoves(index int, board uint64) uint64
	FindAttacks(index int, allies uint64, enemies uint64) uint64
	FindAllAttacks(index int, allies uint64, enemies uint64) uint64
}
