package gameState

import chess.utils.printBitDebug
import gamePieces.RuleBook
import inputHandler.InputHandler

class GameManager(
    var inputHandler: InputHandler = InputHandler(),
    val moveHistory: MutableList<ChessMove> = mutableListOf()
) {
    //TODO: init game (possibly from save file)

    lateinit var bSManager: BoardStateManager
    lateinit var ruleBook: RuleBook

    fun initializeGame(){
        bSManager = BoardStateManager()
        bSManager.setGameManager(this)

        ruleBook = RuleBook(this)
    }

    fun startGameLoop(){
        var playerMove: ChessMove?
        printBitDebug(bSManager.getBoardState(), "::Chess Board::")
        while(true) {
            playerMove = inputHandler.readInput()
            if (playerMove != null) {
                println("Registered Move: " + playerMove.initialCoord + "|" + playerMove.targetCoord)
                playerMove.assignChessPiece(bSManager.getPieceAt(playerMove.initialIndex))
                if (bSManager.execChessMove(playerMove)) {
                    moveHistory.add(playerMove)
                    println("Move was executed successfully!")
                    printBitDebug(bSManager.getBoardState(), "::Chess Board::")
                } else {
                    println("Error! Move was not valid!")
                    continue
                }
            } else {
                println("Error! Move was not valid!")
            }
            //TODO: get chess piece then check if move valid, if yes execute, if no send error and redo readInput
        }
    }

    fun getPrevMove(): Pair<Boolean, ChessMove> {
        if (moveHistory.isEmpty()) {
            return Pair(false, ChessMove())
        }

        return Pair(true,
            moveHistory[moveHistory.lastIndex])
    }
}