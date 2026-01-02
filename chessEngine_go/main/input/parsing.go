package input

import (
	"chessEngine_go/chess/data"
	"chessEngine_go/utils/algebraic"
	"fmt"
	"strings"
)

type Parser struct {
	keywords      []string
	automatedGame [][]string

	automated bool
	gameStep  int
	gameIndex int
}

func NewParser() *Parser {
	var automatedGame = [][]string{
		{"f2f3", "e7e6", "g2g4", "d8h4"},
		{"e2e3", "f7f6", "g1h3", "g7g5", "d1h5"},
		//{"e2e3", "b8a6", "a2a3", "d7d6", "b2b3", "c8e6", "c2c3", "d8d7", "d2d3", "e8c8"},
		//{"g2g3", "a7a6", "f1h3", "b7b6", "g1f3", "c7c6", "e1g1"},
		//{"d2d4", "a7a6", "d4d5", "c7c5", "d5c5"},
		//{"f2f4", "g8h6", "f4f5", "g7g5", "f5g5", "b8c6", "g5g6", "a7a6", "g6g7", "d7d5", "g7g8"},
	}
	return &Parser{automatedGame: automatedGame}
}

func (p *Parser) Initialize() {
	p.automated = false
	p.gameStep = 0
	p.gameIndex = 0
}

func (p *Parser) Read() (bool, data.ChessMove) {
	if p.automated {
		if p.gameStep < len(p.automatedGame[p.gameIndex])-1 {
			p.gameStep++
			var nextMove = algebraic.Split(p.automatedGame[p.gameIndex][p.gameStep])
			return true, *data.NewChessMove(nextMove[0], nextMove[1])
		} else {
			p.automated = false
			p.gameStep = 0
			p.gameIndex = 0
			println("Run of Demo complete. Please reset the game by typing 'reset'.")
		}
	}

	println("Please input a chess movement in algebraic notation: ")
	print("> ")
	var initial, target string

	_, err := fmt.Scan(&initial, &target)
	if err != nil {
		return false, data.NewInvalidMove()
	}
	return p.handleInput(initial, target)
}

func (p *Parser) handleInput(initial string, target string) (bool, data.ChessMove) {
	initial = strings.ToLower(initial)
	target = strings.ToLower(target)

	switch initial {
	case "demo":
		var _, auto = p.handleDemoCmd(target)
		return true, auto
	default:
		if algebraic.IsInAlgebraic(initial) && algebraic.IsInAlgebraic(target) {
			return true, *data.NewChessMove(initial, target)
		}
	}

	return false, data.NewInvalidMove()
}

/*toggles game automation and returns the first move of the chosen automated game*/
func (p *Parser) handleDemoCmd(input string) (bool, data.ChessMove) {
	p.gameIndex = int(input[0] - '0')
	if p.gameIndex >= len(p.automatedGame) {
		println("GameIndex was not valid or does not exist. Automated game cannot be started.")
		return false, data.NewInvalidMove()
	}
	p.automated = true
	var nextMove = algebraic.Split(p.automatedGame[p.gameIndex][p.gameStep])
	return true, *data.NewChessMove(nextMove[0], nextMove[1])
}

/*extracts the first keyword (such as 'help' or 'moves') from the given string*/
func (p *Parser) extractKeyword(input string) string {
	for _, keyword := range p.keywords {
		if strings.Contains(input, keyword) {
			return keyword
		}
	}
	return ""
}

func isMoveInputValid(input string) bool {
	return len(input) == 4
}
