package rules

import (
	"chessEngine_go/chess/data"
	"chessEngine_go/chess/piece"
	"chessEngine_go/chess/piece/movement"
	"chessEngine_go/utils/chessboard"
)

type RuleBook struct {
	Rules []piece.Mover
}

func NewRuleBook() *RuleBook {
	var newRuleBook = RuleBook{
		Rules: []piece.Mover{
			movement.NewMulti(data.WBishop, chessboard.OmniDiagonal),
			movement.NewSingle(data.WKing, chessboard.OmniDirectional),
			movement.NewSingle(data.WKnight, chessboard.KnightPattern),
			movement.NewPawn(data.WPawn, chessboard.OmniDirectional),
			movement.NewMulti(data.WQueen, chessboard.OmniDirectional),
			movement.NewMulti(data.WRook, chessboard.Cardinal),
			movement.NewMulti(data.BBishop, chessboard.OmniDiagonal),
			movement.NewSingle(data.BKing, chessboard.OmniDirectional),
			movement.NewSingle(data.BKnight, chessboard.OmniDiagonal),
			movement.NewPawn(data.BPawn, chessboard.OmniDirectional),
			movement.NewMulti(data.BQueen, chessboard.OmniDirectional),
			movement.NewMulti(data.BRook, chessboard.Cardinal),
		},
	}
	return &newRuleBook
}

func (r *RuleBook) GetRule(chessPiece data.PieceType) piece.Mover {
	return r.Rules[chessPiece]
}
