package movement

import (
	"math/bits"

	"main.go/chess/data"
	"main.go/chess/piece"
	"main.go/utils/bit"
	"main.go/utils/chessboard"
)

type Multi struct {
	Piece data.Piece
}

func NewMulti(pieceType data.PieceType, pattern []int) *Multi {
	return &Multi{*data.NewPiece(pieceType, pattern)}
}

func (m *Multi) CanExecuteMove(move data.ChessMove, board uint64, allies uint64, enemies uint64, simulated bool) (bool, piece.MovementType) {
	var realMoveSet = m.GetMoveSet(move.Initial, board, allies, enemies)
	if simulated {
		realMoveSet.Attack = m.FindAllAttacks(move.Initial, allies, enemies)
	}
	var desiredMove = bit.FlipBit(bit.Empty, move.Target)

	if bits.OnesCount64(realMoveSet.Move&desiredMove) >= 1 {
		return true, piece.Move
	}

	if bits.OnesCount64(realMoveSet.Attack&desiredMove) >= 1 {
		return true, piece.Attack
	}

	return false, piece.Move
}

func (m *Multi) GetMoveSet(index int, board uint64, allies uint64, enemies uint64) data.MoveSet {
	return data.MoveSet{Move: m.FindMoves(index, board), Attack: m.FindAttacks(index, allies, enemies)}
}

func (m *Multi) FindMoves(index int, board uint64) uint64 {
	var moves = bit.Empty

	for _, step := range m.Piece.MovePattern {
		var next = index
		for chessboard.IsWithinBoard(next + step) {
			//fixes file overflow
			if chessboard.WillFileOverflow(next, next+step) {
				break
			}

			next += step
			var move = bit.FlipBit(bit.Empty, next)

			if bits.OnesCount64(move&board) != 0 {
				break
			}

			moves = moves ^ move
		}
	}
	return moves
}

func (m *Multi) FindAttacks(index int, allies uint64, enemies uint64) uint64 {
	var attacks = bit.Empty

	for _, step := range m.Piece.MovePattern {
		var next = index
		for chessboard.IsWithinBoard(next + step) {
			if chessboard.WillFileOverflow(next, next+step) {
				break
			}

			next += step

			var attack = bit.FlipBit(bit.Empty, next)
			if bits.OnesCount64(attack&allies) != 0 {
				break
			}

			attack = attack & enemies
			if bits.OnesCount64(attack) != 0 {
				attacks = attacks ^ attack
				break
			}
		}
	}
	return attacks
}

func (m *Multi) FindAllAttacks(index int, allies uint64, enemies uint64) uint64 {
	var attacks = bit.Empty

	for _, step := range m.Piece.MovePattern {
		var next = index
		for chessboard.IsWithinBoard(next + step) {
			if chessboard.WillFileOverflow(next, next+step) {
				break
			}

			next += step

			var attack = bit.FlipBit(bit.Empty, next) & allies
			if bits.OnesCount64(attack) != 0 {
				attacks = attacks ^ attack
				break
			}

			attack = bit.FlipBit(bit.Empty, next) & bit.Universe
			if bits.OnesCount64(attack) != 0 {
				attacks = attacks | attack
			}

			attack = bit.FlipBit(bit.Empty, next) & enemies
			if bits.OnesCount64(attack) != 0 {
				attacks = attacks | attack
				break
			}
		}
	}
	return attacks
}
