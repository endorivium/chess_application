package data

import (
	"chessEngine_go/utils/algebraic"
	"strconv"
)

type ChessMove struct {
	Piece        PieceType
	InitialCoord string
	TargetCoord  string
	Initial      int
	Target       int
}

func NewChessMove(initial string, target string) *ChessMove {
	var initIndex = algebraic.ToIndex(initial)
	var targetIndex = algebraic.ToIndex(target)

	cm := ChessMove{InitialCoord: initial, TargetCoord: target,
		Initial: initIndex, Target: targetIndex}
	return &cm
}

func (cm ChessMove) String() string {
	var output = "Chess Move: " + cm.InitialCoord + " to " +
		cm.TargetCoord + " > indices: " + strconv.Itoa(cm.Initial) + "|" + strconv.Itoa(cm.Target)
	return output
}
