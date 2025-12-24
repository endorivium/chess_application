package gameState

import inputHandler.InputHandler

class GameManager(
    var inputHandler: InputHandler = InputHandler()
) {
    //responsible for running the game
    //TODO: init game (possibly from save file)
    //TODO: perpetuate game loop

    fun startGame(){
        val playerMove = inputHandler.readInput()
        println(playerMove.startCoord + "|" + playerMove.endCoord)
        //TODO: get chess piece then check if move valid, if yes execute, if no send error and redo readInput
    }
}