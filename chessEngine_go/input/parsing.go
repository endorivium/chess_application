package input

import (
	"fmt"
	"strings"

	"main.go/chess/data"
	"main.go/utils/algebraic"
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
		{"a2a3", "g8h6", "b1c3", "e7e5", "e2e4", "d8g5", "d2d3", "g5c1", "a3a4", "c1d1", "e1d1", "f8a3", "a1c1"},
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
			println("Run of Demo complete. Please close the program or continue from current board state.")
		}
	}

	println("The command will only be registered if there is a space between the two inputs." +
		" Please input a chess movement in algebraic notation (eg. f2 f3): ")
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

/*handleDemoCmd toggles game automation and returns the first move of the chosen automated game*/
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

/*extractKeyword extracts the first keyword (such as 'help' or 'moves') from the given string*/
func (p *Parser) extractKeyword(input string) string {
	for _, keyword := range p.keywords {
		if strings.Contains(input, keyword) {
			return keyword
		}
	}
	return ""
}
