package chessStateManagement

import boardRendering.BoardRenderer
import utils.toAlgebraic
import chessData.ChessMove
import chessData.EPieceType
import inputHandling.EOutputType
import inputHandling.InputHandler

open class GameManager(
    private var inputHandler: InputHandler = InputHandler(),
    private var renderer: BoardRenderer = BoardRenderer(),
    private var bsm: BoardStateManager = BoardStateManager()
) {
    private var whiteTurn = true
    private var gameEnded = false

    private fun initializeGame() {
        inputHandler.initialize()
        renderer.initialize()
        bsm.initialize()
        whiteTurn = true
        gameEnded = false
    }

    /*starts and perpetuates the game loop until game win by either color, then asks for reset to play again*/
    fun startGameLoop() {
        initializeGame()
        var playerMove: Pair<EOutputType, ChessMove?>
        renderer.renderBoard(
            whiteTurn, check = false, checkMate = false,
            board = bsm.getBoardState(), pieceBoards = bsm.getPieceBoards()
        )
        while (!gameEnded) {
            playerMove = inputHandler.readInput()

            if (playerMove.first == EOutputType.Move) handleMove(playerMove.second)
            if (playerMove.first == EOutputType.MoveCheck) handleMoveInquiry(playerMove.second)
            if (playerMove.first == EOutputType.Demo) handleAutomatedMove(playerMove.second)
            if (playerMove.first == EOutputType.Reset) startGameLoop()

            val check = bsm.isCheck(whiteTurn)
            val checkMate = bsm.isCheckmate(whiteTurn)
            gameEnded = checkMate
            renderer.renderBoard(
                whiteTurn, check, checkMate,
                bsm.getBoardState(), bsm.getPieceBoards()
            )
        }
        val playAgain = inputHandler.inquireReplay()
        if (playAgain == EOutputType.Reset) {
            startGameLoop()
        }
    }

    /*handles move output*/
    private fun handleMove(playerMove: ChessMove?) {
        if (playerMove == null) {
            println("Command could not be executed.")
            return
        }

        println("Registered Move: " + playerMove.initialCoord + "|" + playerMove.targetCoord)

        val execResult = bsm.execChessMove(playerMove, whiteTurn)
        if (execResult.first) { //was the execution successful?
            giveOverTurn()
            println("Move was executed successfully!")

            if(execResult.second != null) { //has a pawn reached the end of the chess board?
                renderer.renderBoard(
                    whiteTurn, check = false, checkMate = false,
                    bsm.getBoardState(), bsm.getPieceBoards()
                )

                val chosenTransformIndex = inputHandler.inquirePawnTransform(execResult.second!!.chessPiece)
                val chosenTransform = EPieceType.fromInt(chosenTransformIndex)
                    ?: throw IllegalStateException("Index of chosen pawn transform was null when converting to EPieceType!")
                bsm.execPawnTransformation(playerMove, chosenTransform)
            }
        } else {
            println("Error! Move could not be executed. It is either not your turn or there was no chess piece on that square.")
        }
    }

    /*handles moves from the automated demo games*/
    private fun handleAutomatedMove(playerMove: ChessMove?) {
        if (playerMove == null) return
        handleMove(playerMove)
    }

    /*handles player inquiry about the moveset of a chess piece on the given square*/
    private fun handleMoveInquiry(playerMove: ChessMove?) {
        if (playerMove == null) {
            println(
                "Error! Inquiry was not valid! Was it in algebraic notation " +
                        "(type 'AN' for info) and is there a chess piece on that square?"
            )
            return
        }

        val squares = bsm.findPossibleMoves(playerMove, whiteTurn)

        if (squares.first == null) { //is there a chess piece on the given square?
            println(
                "Error! There is either no chess piece at " +
                        "${toAlgebraic(playerMove.initialIndex)} or it is not one of yours!"
            )
            return
        }

        renderer.printPossibleMoves(squares.first!!, playerMove.initialIndex, squares.second, whiteTurn)
    }

    private fun giveOverTurn(){
        whiteTurn = !whiteTurn
    }
}