package main

import (
	"chessEngine_go/chess/state"
)

func main() {
	var gameState = state.NewGameState()

	gameState.StartGameLoop()
}
