package data

type Piece struct {
	Type        PieceType
	MovePattern []int
}

func NewPiece(pieceType PieceType, pattern []int) *Piece {
	return &Piece{pieceType, pattern}
}
