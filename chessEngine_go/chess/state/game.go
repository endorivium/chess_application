package state

import (
	"main.go/chess/data"
	"main.go/input"
	"main.go/rendering"
)

type GameState struct {
	inputParser *input.Parser
	boardState  *BoardState
	renderer    *rendering.BoardRendering

	WhiteTurn bool
	GameEnded bool
}

func NewGameState() GameState {
	return GameState{inputParser: input.NewParser(), boardState: NewBoardState(), renderer: rendering.NewBoardRendering(),
		WhiteTurn: true}
}

func (gs *GameState) initializeGame() {
	gs.inputParser.Initialize()
	gs.boardState.Initialize()
	gs.WhiteTurn = true
	gs.GameEnded = false
}

/*StartGameLoop starts and perpetuates the game loop until game win by either color, then asks for reset to play again*/
func (gs *GameState) StartGameLoop() {
	gs.initializeGame()
	gs.renderer.RenderBoard(
		gs.WhiteTurn, false, false,
		gs.boardState.GetBoardState(), gs.boardState.GetPieceBoards())
	for !gs.GameEnded {
		output, playerMove := gs.inputParser.Read()
		if output {
			gs.handleMove(playerMove)
		} else {
			println("Error! Move could not be executed. " +
				"Make sure to format your input in algebraic notation ([move]space[move])" +
				" including the space and without any additional words, e.g. f2 f3 ")
		}

		var check = gs.boardState.IsCheck(gs.WhiteTurn)
		var checkMate = gs.boardState.IsCheckmate(gs.WhiteTurn)
		gs.GameEnded = checkMate
		gs.renderer.RenderBoard(
			gs.WhiteTurn, check, checkMate,
			gs.boardState.GetBoardState(), gs.boardState.GetPieceBoards())
	}
}

/*handleMove handles move output*/
func (gs *GameState) handleMove(playerMove data.ChessMove) {
	if playerMove.Initial == -1 {
		println("Command could not be executed.")
		return
	}

	println("Registered Move: " + playerMove.InitialCoord + "|" + playerMove.TargetCoord)

	var execResult = gs.boardState.execChessMove(playerMove, gs.WhiteTurn)
	if execResult { //was the execution successful?
		gs.giveOverTurn()
		println("Move was executed successfully!")
	} else {
		println("Error! Move could not be executed.")
	}
}

func (gs *GameState) giveOverTurn() {
	gs.WhiteTurn = !gs.WhiteTurn
}
