package gameState

import bitoperation.utils.printBitDebug
import gamePieces.RuleSet
import inputHandler.InputHandler

class GameManager(
    var inputHandler: InputHandler = InputHandler(),
    val moveHistory: MutableList<ChessMove> = mutableListOf()
) {
    //TODO: init game (possibly from save file)
    //TODO: perpetuate game loop

    lateinit var bSManager: BoardStateManager
    lateinit var ruleSet: RuleSet

    fun initializeGame(){
        bSManager = BoardStateManager()
        bSManager.setGameManager(this)

        ruleSet = RuleSet(this)
    }

    fun startGameLoop(){
        var playerMove: ChessMove? = null
        while(true) {
            playerMove = inputHandler.readInput()
            if (playerMove != null) {
                println("Registered Move: " + playerMove.initialCoord + "|" + playerMove.targetCoord)
                if (bSManager.executeChessMove(playerMove)) {
                    moveHistory.add(playerMove)
                    printBitDebug(bSManager.getBoardState(), "::Chess Board::")
                    println("Move was executed successfully!")
                } else {
                    println("Move was not valid!")
                    continue
                }
            } else {
                throw IllegalStateException("Player Move was null!")
            }
            //TODO: get chess piece then check if move valid, if yes execute, if no send error and redo readInput
        }
    }

    fun getPrevMove(): Pair<Boolean, ChessMove>{
        if(moveHistory.isEmpty()){
            return Pair(false, ChessMove())
        }

        return Pair(true,
            moveHistory[moveHistory.size - 1])
    }
}