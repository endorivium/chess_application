package rendering

import (
	"chessEngine_go/utils/bit"
	"chessEngine_go/utils/chessboard"
)

type BoardRendering struct {
	pieceIcons []string
	noPiece    string
	rank       []string
	file       string
}

func NewBoardRendering() *BoardRendering {
	var boardRenderer = BoardRendering{
		pieceIcons: []string{"[wBi]", "[wKi]", "[wKn]", "[wPa]", "[wQu]", "[wRo]",
			"[bBi]", "[bKi]", "[bKn]", "[bPa]", "[bQu]", "[bRo]"},
		noPiece: "[===]",
		rank:    []string{"[8]", "[7]", "[6]", "[5]", "[4]", "[3]", "[2]", "[1]"},
		file:    "     [ A ][ B ][ C ][ D ][ E ][ F ][ G ][ H ]",
	}
	return &boardRenderer
}

func (br *BoardRendering) RenderBoard(
	whiteTurn bool, check bool, checkMate bool,
	board uint64, pieceBoards []uint64,
) {
	var boardRender = br.refreshRendering(board, pieceBoards)

	print("\n")
	print("    =============  ")
	if whiteTurn {
		print("WHITE")
	} else {
		print("BLACK")
	}
	print("'S TURN  =============\n")
	var rankIndex = 0
	for _, render := range boardRender {
		print("\n" + br.rank[rankIndex] + "  " + render)
		rankIndex++
	}
	println("\n" + br.file)
	if checkMate {
		br.printCheckMate(whiteTurn)
		println("\n")
		return
	}
	if check {
		br.printCheck()
	}
	print("\n")
}

func (br *BoardRendering) refreshRendering(board uint64, pieceBoards []uint64) []string {
	var indices = chessboard.GetBoardIndices(board)

	pieceRender := make([]string, 64)
	for i := range pieceRender {
		pieceRender[i] = br.noPiece
	}

	for _, i := range indices {
		for p, _ := range pieceBoards {
			if bit.GetBit(pieceBoards[p], i) {
				pieceRender[i] = br.pieceIcons[p]
			}
		}
	}
	return br.summarizeRanks(pieceRender)
}

func (br *BoardRendering) summarizeRanks(pieces []string) []string {
	rankRender := make([]string, 8)
	for i, _ := range rankRender {
		var rank = ""

		for j := i * 8; j <= i*8+7; j++ {
			rank += pieces[j]
		}
		rankRender[7-i] = rank
	}
	return rankRender
}

func (br *BoardRendering) printCheck() {
	print("\n    =============  " +
		"KING IN CHECK" +
		"  =============")
}

func (br *BoardRendering) printCheckMate(whiteTurn bool) {
	print(
		"\n    =============  " +
			"CHECKMATE!" +
			"  =============\n")
	if whiteTurn {
		print(
			"    =============  " +
				"BLACK WINS!" +
				"  =============")
	} else {
		print(
			"    =============  " +
				"WHITE WINS!" +
				"  =============")
	}
}
