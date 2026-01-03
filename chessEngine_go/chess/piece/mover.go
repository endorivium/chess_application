package piece

import (
	"main.go/chess/data"
)

type Mover interface {
	/*CanExecuteMove returns true if the desired move can be executed and what type it is*/
	CanExecuteMove(move data.ChessMove, board uint64, allies uint64, enemies uint64, simulated bool) (bool, MovementType)
	/*GetMoveSet returns move set with possible moves and attacks*/
	GetMoveSet(index int, board uint64, allies uint64, enemies uint64) data.MoveSet
	/*FindMoves returns all possible moves of the chess piece from its position on the board*/
	FindMoves(index int, board uint64) uint64
	/*FindAttacks returns all possible attacks of the chess piece from its position on the board*/
	FindAttacks(index int, allies uint64, enemies uint64) uint64
	/*FindAllAttacks returns all possible attacks of the chess piece from its position on the board,
	including the nearest spaces of ally pieces (used for simulation)*/
	FindAllAttacks(index int, allies uint64, enemies uint64) uint64
}
