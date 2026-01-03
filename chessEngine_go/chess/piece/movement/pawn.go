package movement

import (
	"math/bits"

	"main.go/chess/data"
	"main.go/chess/piece"
	"main.go/utils/bit"
	"main.go/utils/chessboard"
)

type Pawn struct {
	Piece data.Piece
	mod   int
}

func NewPawn(pieceType data.PieceType) *Pawn {
	var isWhite = chessboard.IsWhite(pieceType)
	var newPawn = Pawn{Piece: *data.NewPiece(pieceType, chessboard.OmniDirectional)}
	if isWhite {
		newPawn.mod = 1
	} else {
		newPawn.mod = -1
	}
	return &newPawn
}

func (p *Pawn) CanExecuteMove(move data.ChessMove, board uint64, allies uint64, enemies uint64, simulated bool) (bool, piece.MovementType) {
	var realMoveSet = p.GetMoveSet(move.Initial, board, allies, enemies)
	if simulated {
		realMoveSet.Attack = p.FindAllAttacks(move.Initial, allies, enemies)
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

func (p *Pawn) FindMoves(index int, board uint64) uint64 {
	var singlePush = p.pushSingle(index)
	var doublePush = p.pushDouble(index)
	var forwardMoves = singlePush ^ doublePush
	forwardMoves = (forwardMoves ^ board) & forwardMoves

	return forwardMoves
}

func (p *Pawn) FindAttacks(index int, allies uint64, enemies uint64) uint64 {
	var leftAttack = bit.Empty
	if !chessboard.IsFile('A', index) && chessboard.IsWithinRanks(index, 2, 7) {
		leftAttack = bit.FlipBit(bit.Empty, index+p.mod*7)
	}
	leftAttack = leftAttack & enemies

	var rightAttack = bit.Empty
	if !chessboard.IsFile('H', index) && chessboard.IsWithinRanks(index, 2, 7) {
		rightAttack = bit.FlipBit(bit.Empty, index+p.mod*9)
	}
	rightAttack = rightAttack & enemies

	return leftAttack | rightAttack //^ p.enPassantMove(index)
}

func (p *Pawn) FindAllAttacks(index int, allies uint64, enemies uint64) uint64 {
	var leftAttack = bit.Empty
	if !chessboard.IsFile('A', index) && chessboard.IsWithinRanks(index, 2, 7) {
		leftAttack = bit.FlipBit(bit.Empty, index+p.mod*7)
	}

	var rightAttack = bit.Empty
	if !chessboard.IsFile('H', index) && chessboard.IsWithinRanks(index, 2, 7) {
		rightAttack = bit.FlipBit(bit.Empty, index+p.mod*9)
	}

	return leftAttack | rightAttack
}

func (p *Pawn) GetMoveSet(index int, board uint64, allies uint64, enemies uint64) data.MoveSet {
	var push = p.FindMoves(index, board)
	var attack = p.FindAttacks(index, allies, enemies)
	var possibleMoves = (push ^ board) & push

	return data.MoveSet{Move: possibleMoves, Attack: attack}
}

func (p *Pawn) pushSingle(index int) uint64 {
	var singlePush = bit.Empty
	if chessboard.IsWithinRanks(index, 2, 7) {
		return bit.FlipBit(singlePush, index+p.mod*8)
	}
	return singlePush
}

func (p *Pawn) pushDouble(index int) uint64 {
	var doublePush = bit.Empty
	if chessboard.IsRank(2, index) && chessboard.IsWhite(p.Piece.Type) ||
		chessboard.IsRank(7, index) && !chessboard.IsWhite(p.Piece.Type) {
		doublePush = bit.FlipBit(doublePush, index+p.mod*16)
	}
	return doublePush
}
