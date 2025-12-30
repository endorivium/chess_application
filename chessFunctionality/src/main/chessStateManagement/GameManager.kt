package main.chessStateManagement

import main.boardRendering.BoardRenderer
import main.utils.toAlgebraic
import main.chessData.ChessMove
import main.chessData.EPieceType
import main.chessPieceImplementation.RuleBook
import main.chessPieceImplementation.baseImplementation.ChessPiece
import main.inputHandling.EOutputType
import main.inputHandling.InputHandler

class GameManager(
    var inputHandler: InputHandler = InputHandler(),
    val moveHistory: MutableList<ChessMove> = mutableListOf()
) {
    //TODO: init game (possibly from save file)

    private lateinit var bsm: BoardStateManager
    private lateinit var ruleBook: RuleBook
    private lateinit var renderer: BoardRenderer

    var whiteTurn = true
    var gameEnded = false

    fun initializeGame() {
        inputHandler.initialize()
        bsm = BoardStateManager(this)
        bsm.initialize()
        ruleBook = RuleBook(this)
        renderer = BoardRenderer(this)
        whiteTurn = true
        gameEnded = false
    }

    fun startGameLoop() {
        initializeGame()
        var playerMove: Pair<EOutputType, ChessMove?>
        renderer.renderBoard(whiteTurn, check = false, checkMate = false)
        while (!gameEnded) {
            playerMove = inputHandler.readInput()

            if(playerMove.first == EOutputType.Move) handleMove(playerMove.second)
            if(playerMove.first == EOutputType.MoveCheck) handleMoveInquiry(playerMove.second)
            if(playerMove.first == EOutputType.Demo) handleAutomatedMove(playerMove.second)
            if(playerMove.first == EOutputType.Reset) startGameLoop()

            val check = bsm.isCheck(whiteTurn)
            val checkMate = bsm.isCheckMate(whiteTurn)
            renderer.renderBoard(whiteTurn, check, checkMate)
            gameEnded = checkMate
        }
        //TODO("as if wants to play again via input handler")
        val playAgain = inputHandler.playAgain()
        if (playAgain == EOutputType.Reset) {
            startGameLoop()
        }
    }



    fun handleAutomatedMove(playerMove: ChessMove?) {
        if(playerMove == null) return
        handleMove(playerMove)
    }

    fun handleMoveInquiry(playerMove: ChessMove?) {
        if (playerMove == null) {
            println("Error! Inquiry was not valid! Was it in algebraic notation " +
                    "(type 'AN' for info) and is there a chess piece on that square?")
            return
        }

        val squares = bsm.findPossibleMoves(playerMove, whiteTurn)
        val chessPiece = bsm.getPieceAt(playerMove.initialIndex)

        if(chessPiece == null) {
            println("Error! There is either no chess piece at " +
                    "${toAlgebraic(playerMove.initialIndex)} or it is not one of yours!")
            return
        }

        renderer.printPossibleMoves(chessPiece, playerMove.initialIndex, squares.second, whiteTurn)
    }

    fun handleMove(playerMove: ChessMove?) {
        if (playerMove == null) {
            println("Error! Move was not formatted correctly")
            return
        }

        println("Registered Move: " + playerMove.initialCoord + "|" + playerMove.targetCoord)
        playerMove.assignChessPiece(bsm.getPieceAt(playerMove.initialIndex))

        if (bsm.execChessMove(playerMove, whiteTurn)) {
            moveHistory.add(playerMove)

            whiteTurn = !whiteTurn
            println("Move was executed successfully!")
        } else {
            println("Error! Move could not be executed. It is either not your turn or there was no chess piece on that square.")
        }
    }

    fun getPrevMove(): Pair<Boolean, ChessMove> {
        if (moveHistory.isEmpty()) {
            return Pair(false, ChessMove())
        }

        return Pair(
            true,
            moveHistory[moveHistory.lastIndex]
        )
    }

    fun getBSM(): BoardStateManager {
        return bsm
    }

    fun getRules(chessPiece: EPieceType): ChessPiece {
        val pieceRules = ruleBook.rules[chessPiece]
            ?: throw IllegalArgumentException("Chess Piece $chessPiece was not found in Rule Set!")
        return pieceRules
    }
}