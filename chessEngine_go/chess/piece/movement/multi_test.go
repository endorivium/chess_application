package movement

import (
	"testing"

	"main.go/chess/data"
	"main.go/utils/chessboard"
)

func TestMulti_FindMoves(t *testing.T) {
	multi := NewMulti(data.WQueen, chessboard.OmniDirectional)
	var board uint64 = 0xffff00000000ffff

	result := multi.FindMoves(30, board)
	var expected uint64 = 0x7fd070a0000

	if result != expected {
		t.Errorf("WQueen (multi) returned %d instead of %d", result, expected)
	}
}

func TestMulti_FindAttacks(t *testing.T) {
	multi := NewMulti(data.WQueen, chessboard.OmniDirectional)
	var allyBoard uint64 = 0xffff000000000000
	var enemyBoard uint64 = 0xffff

	result := multi.FindAttacks(30, allyBoard, enemyBoard)
	var expected uint64 = 0x1200

	if result != expected {
		t.Errorf("WQueen (multi) returned %d instead of %d", result, expected)
	}
}

func TestMulti_FindAllAttacks(t *testing.T) {
	multi := NewMulti(data.WQueen, chessboard.OmniDirectional)
	var allyBoard uint64 = 0xffff000000000000
	var enemyBoard uint64 = 0xffff

	result := multi.FindAllAttacks(17, allyBoard, enemyBoard)
	var expected uint64 = 0xe0bfe050484400

	if result != expected {
		t.Errorf("WQueen (multi) returned %d instead of %d", result, expected)
	}
}
