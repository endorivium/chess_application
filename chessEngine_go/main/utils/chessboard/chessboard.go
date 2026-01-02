package chessboard

import (
	"chessEngine_go/chess/data"
	"chessEngine_go/utils/bit"
	"unicode"
)

var OmniDirectional = []int{7, 8, 9, -1, 1, -9, -8, -7}
var OmniDiagonal = []int{7, 9, -7, -9}
var KnightPattern = []int{6, 15, 17, 10, -10, -17, -15, -6}
var Cardinal = []int{1, 8, -1, -8}

func IsWithinRanks(index int, lowerRank int, upperRank int) bool {
	var rank = index/8 + 1
	if rank >= lowerRank && rank <= upperRank {
		return true
	}
	return false
}

func IsWithinBoard(index int) bool {
	return index >= 0 && index <= 63
}

func IsFile(file rune, index int) bool {
	file = unicode.ToLower(file)
	var intFile = int(file - 97)
	return intFile == index%8
}

func IsRank(rank int, index int) bool {
	return index/8+1 == rank
}

/*GetBoardIndices returns indices of all marked bits in the bitboard*/
func GetBoardIndices(board uint64) []int {
	var indices []int

	for i := 0; i <= 63; i++ {
		if bit.GetBit(board, i) {
			indices = append(indices, i)
		}
	}
	return indices
}

func IsWhite(piece data.PieceType) bool {
	return piece >= 0 && piece <= 5
}

/*WillFileOverflow returns true if the given index will overflow the file, jumping from A to H or vice versa*/
func WillFileOverflow(current int, next int) bool {
	var currFile = current % 8
	var nextFile = next % 8
	var dist = Abs(currFile - nextFile)

	return dist > 2
}

func Abs(value int) int {
	if value < 0 {
		return -value
	}
	return value
}
