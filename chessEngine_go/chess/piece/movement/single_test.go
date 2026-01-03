package movement

import (
	"testing"

	"main.go/chess/data"
	"main.go/utils/chessboard"
)

func TestSingle_FindMoves(t *testing.T) {
	single := NewSingle(data.BKing, chessboard.OmniDirectional)
	var board uint64 = 0xffff00000000ffff

	result := single.FindMoves(17, board)
	var expected uint64 = 0xa0e000000000

	if result != expected {
		t.Errorf("BKing (single) returned %d instead of %d", result, expected)
	}
}

func TestSingle_FindAttacks(t *testing.T) {
	single := NewSingle(data.BKing, chessboard.OmniDirectional)
	var allyBoard uint64 = 0xffff
	var enemyBoard uint64 = 0xffff000000000000

	result := single.FindAttacks(17, allyBoard, enemyBoard)
	var expected uint64 = 0xe0000000000000

	if result != expected {
		t.Errorf("BKing (single) returned %d instead of %d", result, expected)
	}
}

func TestSingle_FindAllAttacks(t *testing.T) {
	single := NewSingle(data.BKing, chessboard.OmniDirectional)
	var allyBoard uint64 = 0xffff
	var enemyBoard uint64 = 0xffff000000000000

	result := single.FindAllAttacks(17, allyBoard, enemyBoard)
	var expected uint64 = 0xe0a0e000000000

	if result != expected {
		t.Errorf("BKing (single) returned %d instead of %d", result, expected)
	}
}
