package data

type PieceType int

const (
	WBishop PieceType = iota
	WKing
	WKnight
	WPawn
	WQueen
	WRook
	BBishop
	BKing
	BKnight
	BPawn
	BQueen
	BRook
)

var pieceName = map[PieceType]string{
	WBishop: "White Bishop",
	WKing:   "White King",
	WKnight: "White Knight",
	WPawn:   "White Pawn",
	WQueen:  "White Queen",
	WRook:   "White Rook",
	BBishop: "Black Bishop",
	BKing:   "Black King",
	BKnight: "Black Knight",
	BPawn:   "Black Pawn",
	BQueen:  "Black Queen",
	BRook:   "Black Rook",
}

func (pt PieceType) String() string {
	return pieceName[pt]
}
