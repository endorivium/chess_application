package state

import (
	"testing"

	"main.go/chess/rules"
	"main.go/utils/bit"
	"main.go/utils/chessboard"
)

func TestBoardState_AreSquaresThreatened(t *testing.T) {
	boardState := BoardState{ruleBook: rules.NewRuleBook(),
		boards: []uint64{bit.Empty, bit.Empty, bit.Empty, 0x20020008200400, bit.Empty, bit.Empty,
			bit.Empty, bit.Empty, 0x800000000000000, bit.Empty, 0x8, 0x100000000000}}

	result := boardState.AreSquaresThreatened(chessboard.GetBoardIndices(0x20020008200400), true)

	if !result {
		t.Errorf("Result returned false when it should have been true.")
	}
}

func TestBoardState_IsCheck(t *testing.T) {
	boardState := BoardState{ruleBook: rules.NewRuleBook(),
		boards: []uint64{bit.Empty, 0x80000, bit.Empty, bit.Empty, bit.Empty, bit.Empty,
			0x400000000000, bit.Empty, bit.Empty, bit.Empty, bit.Empty, bit.Empty}}

	result := boardState.IsCheck(true)

	if !result {
		t.Errorf("Result returned false when it should have been true.")
	}
}

func TestBoardState_IsCheckmate(t *testing.T) {
	boardState := BoardState{ruleBook: rules.NewRuleBook(),
		boards: []uint64{bit.Empty, 0x20, bit.Empty, bit.Empty, bit.Empty, bit.Empty,
			0x2000000, bit.Empty, bit.Empty, bit.Empty, 0x4000, 0x1080}}

	result := boardState.IsCheckmate(true)

	if !result {
		t.Errorf("Result returned false when it should have been true.")
	}
}
