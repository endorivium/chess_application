package chessStateManager

import boardRendering.BoardRenderer
import chess.utils.toAlgebraic
import chessData.ChessMove
import chessPieces.RuleBook
import inputHandler.EOutputType
import inputHandler.InputHandler

class GameManager(
    var inputHandler: InputHandler = InputHandler(),
    val moveHistory: MutableList<ChessMove> = mutableListOf()
) {
    //TODO: init game (possibly from save file)

    lateinit var bsm: BoardStateManager
    lateinit var ruleBook: RuleBook
    lateinit var renderer: BoardRenderer

    var whiteTurn = true

    fun initializeGame() {
        bsm = BoardStateManager(this)

        ruleBook = RuleBook(this)
        renderer = BoardRenderer(this)
    }

    fun startGameLoop() {
        var playerMove: Pair<EOutputType, ChessMove?>
        renderer.renderBoard(whiteTurn)
        while (true) {
            playerMove = inputHandler.readInput()

            if(playerMove.first == EOutputType.Move) handleMoveOutput(playerMove.second)
            if(playerMove.first == EOutputType.MoveCheck) handleMoveInquiry(playerMove.second)

            renderer.renderBoard(whiteTurn)
        }
    }

    fun handleMoveInquiry(playerMove: ChessMove?) {
        if (playerMove == null) {
            println("Error! Inquiry was not valid! It most likely was not in algebraic notation (type 'AN' for info)")
            return
        }

        val squares = bsm.findPossibleMoves(playerMove, whiteTurn)
        val chessPiece = bsm.getPieceAt(playerMove.initialIndex)

        if(chessPiece == null) {
            println("Error! There is either no chess piece at " +
                    "${toAlgebraic(playerMove.initialIndex)} or it is not one of yours!")
            return
        }

        inputHandler.printCheck(chessPiece, playerMove.initialIndex, squares.second)
    }

    fun handleMoveOutput(playerMove: ChessMove?) {
        if (playerMove == null) {
            println("Error! Move was not valid!")
            return
        }

        println("Registered Move: " + playerMove.initialCoord + "|" + playerMove.targetCoord)
        playerMove.assignChessPiece(bsm.getPieceAt(playerMove.initialIndex))

        if (bsm.execChessMove(playerMove, whiteTurn)) {
            moveHistory.add(playerMove)

            whiteTurn = !whiteTurn
            println("Move was executed successfully!")
        } else {
            println("Error! Move was not valid!")
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
}