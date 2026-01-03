package rules

import (
	"main.go/chess/data"
	"main.go/chess/piece"
	"main.go/chess/piece/movement"
	"main.go/utils/chessboard"
)

type RuleBook struct {
	Rules []piece.Mover
}

func NewRuleBook() RuleBook {
	var newRuleBook = RuleBook{
		Rules: []piece.Mover{
			movement.NewMulti(data.WBishop, chessboard.OmniDiagonal),
			movement.NewSingle(data.WKing, chessboard.OmniDirectional),
			movement.NewSingle(data.WKnight, chessboard.KnightPattern),
			movement.NewPawn(data.WPawn),
			movement.NewMulti(data.WQueen, chessboard.OmniDirectional),
			movement.NewMulti(data.WRook, chessboard.Cardinal),
			movement.NewMulti(data.BBishop, chessboard.OmniDiagonal),
			movement.NewSingle(data.BKing, chessboard.OmniDirectional),
			movement.NewSingle(data.BKnight, chessboard.KnightPattern),
			movement.NewPawn(data.BPawn),
			movement.NewMulti(data.BQueen, chessboard.OmniDirectional),
			movement.NewMulti(data.BRook, chessboard.Cardinal),
		},
	}
	return newRuleBook
}

/*GetRule returns the movement rule of the given chess piece type*/
func (r *RuleBook) GetRule(chessPiece data.PieceType) piece.Mover {
	return r.Rules[chessPiece]
}
