package chessboard

import (
	"chessEngine_go/chess/data"
	"chessEngine_go/utils/bit"
)

func IsWithinRanks(index int, lowerRank int, upperRank int) bool {
	var rank = index/8 + 1
	return rank >= lowerRank && rank <= upperRank
}

func IsWithinBoard(index int) bool {
	return index >= 0 && index <= 63
}

func IsFile(file rune, index int) bool {
	var intFile = int(file - 97)
	return intFile == index%8
}

func IsRank(rank int, index int) bool {
	return index/8+1 == rank
}

/*returns indices of all marked bits in the bitboard*/
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

/*returns if the given index will overflow the file, jumping from A to H or vice versa*/
func WillFileOverflow(current int, next int) bool {
	var currFile = current % 8
	var nextFile = next % 8
	var dist = currFile - nextFile
	if dist < 0 {
		dist = -dist
	}

	return dist > 2
}
