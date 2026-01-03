package movement

import (
	"testing"

	"main.go/chess/data"
)

func TestPawn_FindMoves(t *testing.T) {
	pawn := NewPawn(data.WPawn)
	var board uint64 = 0xffff00000000ffff

	result := pawn.FindMoves(15, board)
	var expected uint64 = 0x10100000000

	if result != expected {
		t.Errorf("WPawn (pawn) returned %d instead of %d", result, expected)
	}
}

func TestPawn_FindAttacks(t *testing.T) {
	pawn := NewPawn(data.WPawn)
	var allyBoard uint64 = 0x1b0b220000000000
	var enemyBoard uint64 = 0xc00200eac00

	result := pawn.FindAttacks(13, allyBoard, enemyBoard)
	var expected uint64 = 0x80000000000

	if result != expected {
		t.Errorf("WPawn (pawn) returned %d instead of %d", result, expected)
	}
}

func TestPawn_FindAllAttacks(t *testing.T) {
	pawn := NewPawn(data.WPawn)
	var allyBoard uint64 = 0x1b0b220000000000
	var enemyBoard uint64 = 0xc00200eac00

	result := pawn.FindAllAttacks(13, allyBoard, enemyBoard)
	var expected uint64 = 0xa0000000000

	if result != expected {
		t.Errorf("WPawn (pawn) returned %d instead of %d", result, expected)
	}
}
