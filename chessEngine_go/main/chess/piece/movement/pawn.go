package movement

import (
	"chessEngine_go/chess/data"
	"chessEngine_go/chess/piece"
	"chessEngine_go/utils/bit"
	"chessEngine_go/utils/chessboard"
	"math/bits"
)

type Pawn struct {
	Piece data.Piece
	mod   int
}

func NewPawn(pieceType data.PieceType, pattern []int) *Pawn {
	var isWhite = chessboard.IsWhite(pieceType)
	var newPawn = Pawn{Piece: *data.NewPiece(pieceType, pattern)}
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

	if bits.OnesCount64(realMoveSet.Rochade&desiredMove) >= 1 {
		return true, piece.Rochade
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

//
//func (p *Pawn) enPassantMove(index int) uint64 {
//	var ok, prevMove = p.boardState.GetPrevMove()
//	if !ok {
//		return bit.Empty
//	}
//
//	return p.leftEnPassant(index, prevMove) ^ p.rightEnPassant(index, prevMove)
//}
//
//func (p *Pawn) leftEnPassant(index int, prevMove data.ChessMove) uint64 {
//	var stepDist = chessboard.Abs(prevMove.Initial/8 - prevMove.Target/8)
//
//	var enPassantIndexLeft int
//	if !chessboard.IsFile('A', index) {
//		enPassantIndexLeft = index - 1
//	} else {
//		enPassantIndexLeft = -1
//	}
//
//	if stepDist != 2 || enPassantIndexLeft == -1 {
//		return bit.Empty
//	}
//
//	if prevMove.Piece == p.getEnemyPawn() && prevMove.Target == enPassantIndexLeft {
//		return bit.FlipBit(bit.Empty, enPassantIndexLeft)
//	}
//	return bit.Empty
//}
//
//func (p *Pawn) rightEnPassant(index int, prevMove data.ChessMove) uint64 {
//	var stepDist = chessboard.Abs(prevMove.Initial/8 - prevMove.Target/8)
//	var enPassantIndexRight int
//	if !chessboard.IsFile('H', index) {
//		enPassantIndexRight = index + 1
//	} else {
//		enPassantIndexRight = -1
//	}
//
//	if stepDist != 2 || enPassantIndexRight == -1 {
//		return bit.Empty
//	}
//
//	if prevMove.Piece == p.getEnemyPawn() && prevMove.Target == enPassantIndexRight {
//		return bit.FlipBit(bit.Empty, enPassantIndexRight)
//	}
//	return bit.Empty
//}

func (p *Pawn) getEnemyPawn() data.PieceType {
	if p.Piece.Type == data.BPawn {
		return data.WPawn
	} else {
		return data.BPawn
	}
}

//func (p *Pawn) notifyTransformation() {
//	p.boardState.NotifyPawnTransformation()
//}
