package movement

import (
	"math/bits"

	"main.go/chess/data"
	"main.go/chess/piece"
	"main.go/utils/bit"
	"main.go/utils/chessboard"
)

type Single struct {
	Piece data.Piece
}

func NewSingle(pieceType data.PieceType, pattern []int) *Single {
	return &Single{*data.NewPiece(pieceType, pattern)}
}

func (s *Single) CanExecuteMove(move data.ChessMove, board uint64, allies uint64, enemies uint64, simulated bool) (bool, piece.MovementType) {
	var realMoveSet = s.GetMoveSet(move.Initial, board, allies, enemies)
	if simulated {
		realMoveSet.Attack = s.FindAllAttacks(move.Initial, allies, enemies)
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

func (s *Single) GetMoveSet(index int, board uint64, allies uint64, enemies uint64) data.MoveSet {
	return data.MoveSet{Move: s.FindMoves(index, board), Attack: s.FindAttacks(index, allies, enemies)}
}

func (s *Single) FindMoves(index int, board uint64) uint64 {
	var moves = bit.Empty

	for _, step := range s.Piece.MovePattern {
		if chessboard.WillFileOverflow(index, index+step) || !chessboard.IsWithinBoard(index+step) {
			continue
		}

		var found = bit.FlipBit(bit.Empty, index+step)

		if bits.OnesCount64(found&board) != 0 {
			continue
		}

		moves = moves ^ found
	}
	return moves
}

func (s *Single) FindAttacks(index int, allies uint64, enemies uint64) uint64 {
	var attacks = bit.Empty

	for _, step := range s.Piece.MovePattern {
		if chessboard.WillFileOverflow(index, index+step) || !chessboard.IsWithinBoard(index+step) {
			continue
		}

		var attack = bit.FlipBit(bit.Empty, index+step)
		if bits.OnesCount64(attack&allies) != 0 {
			continue
		}

		attack = attack & enemies
		if bits.OnesCount64(attack) != 0 {
			attacks = attacks ^ attack
		}
	}
	return attacks
}

func (s *Single) FindAllAttacks(index int, allies uint64, enemies uint64) uint64 {
	var attacks = bit.Empty

	for _, step := range s.Piece.MovePattern {
		if chessboard.WillFileOverflow(index, index+step) || !chessboard.IsWithinBoard(index+step) {
			continue
		}

		var attack = bit.FlipBit(bit.Empty, index+step) & allies
		if bits.OnesCount64(attack) != 0 {
			attacks = attacks ^ attack
			continue
		}

		attack = bit.FlipBit(bit.Empty, index+step) & bit.Universe
		if bits.OnesCount64(attack) != 0 {
			attacks = attacks ^ attack
			continue
		}

		attack = bit.FlipBit(bit.Empty, index+step) & enemies
		if bits.OnesCount64(attack) != 0 {
			attacks = attacks ^ attack
			continue
		}
	}
	return attacks
}
