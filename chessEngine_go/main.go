package main

import (
	"main.go/chess/state"
)

/*Comment: all go code is based on the kotlin implementation*/

func main() {
	var gameState = state.NewGameState()

	gameState.StartGameLoop()
}
