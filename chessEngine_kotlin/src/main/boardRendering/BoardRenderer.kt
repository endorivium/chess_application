package boardRendering

import utils.getBit
import utils.getBoardIndices
import utils.isWhite
import utils.toAlgebraic
import chessData.EPieceType

open class BoardRenderer() {
    protected lateinit var pieceIcons: Array<String>
    protected lateinit var noPiece: String
    protected lateinit var rank: Array<String>
    protected lateinit var file: String

    open fun initialize() {
        pieceIcons = arrayOf(
            "[\u265D]", "[\u265A]", "[\u265E]", "[\u265F]", "[\u265B]", "[\u265C]",
            "[\u2657]", "[\u2654]", "[\u2658]", "[\u2659]", "[\u2655]", "[\u2656]"
        )

        noPiece = "[\uFF1D]"
        rank = arrayOf(
            "[\uFF18]",
            "[\uFF17]",
            "[\uFF16]",
            "[\uFF15]",
            "[\uFF14]",
            "[\uFF13]",
            "[\uFF12]",
            "[\uFF11]"
        )
        file = " \uFFE3   [\uFF21][\uFF22][\uFF23][\uFF24][\uFF25][\uFF26][\uFF27][\uFF28]"
    }

    open fun renderBoard(
        whiteTurn: Boolean, check: Boolean, checkMate: Boolean,
        board: ULong, pieceBoards: Array<ULong>
    ) {
        val boardRender = refreshRendering(board, pieceBoards)

        print("\n")
        print("     \uFF1D\uFF1D  ")
        if (whiteTurn) {
            print("\uFF37\uFF28\uFF29\uFF34\uFF25")
        } else {
            print("\uFF22\uFF2C\uFF21\uFF23\uFF2B")
        }
        print("\uFF07\uFF33  \uFF34\uFF35\uFF32\uFF2E  \uFF1D\uFF1D")
        var rankIndex = 0
        for (i in boardRender.indices) {
            print("\n" + rank[rankIndex] + "  " + boardRender[i])
            rankIndex++
        }
        println("\n" + file)
        if (checkMate) {
            printCheckMate(whiteTurn)
            println("\n")
            return
        }
        if (check) {
            printCheck()
        }
        print("\n")
    }

    fun refreshRendering(board: ULong, pieceBoards: Array<ULong>): Array<String> {
        val indices = getBoardIndices(board)
        val pieceRender = Array(64) { noPiece }

        for (index in indices) {
            for (p in pieceBoards.indices) {
                if (getBit(pieceBoards[p], index)) {
                    pieceRender[index] = pieceIcons[p]
                }
            }
        }
        return summarizeRanks(pieceRender)
    }

    fun summarizeRanks(pieces: Array<String>): Array<String> {
        val rankRender = Array(8) { "" }
        for (i in rankRender.indices) {
            var rank = ""
            for (j in i * 8..i * 8 + 7) {
                rank += pieces[j]
            }
            rankRender[7 - i] = rank
        }
        return rankRender
    }

    open fun printCheck() {
        print(
            "\n    \uFF1D\uFF1D  " +
                    "\uFF2B\uFF29\uFF2E\uFF27 \uFF29\uFF2E \uFF23\uFF28\uFF25\uFF23\uFF2B\uFF01" +
                    "  \uFF1D\uFF1D "
        )
    }

    open fun printCheckMate(whiteTurn: Boolean) {
        print(
            "\n      \uFF1D\uFF1D  " +
                    "\uFF23\uFF28\uFF25\uFF23\uFF2B\uFF2D\uFF21\uFF34\uFF25\uFF01" +
                    "  \uFF1D\uFF1D "
        )
        if (!whiteTurn) {
            print(
                "\n      \uFF1D\uFF1D  " +
                        "\uFF37\uFF28\uFF29\uFF34\uFF25 \uFF37\uFF29\uFF2E\uFF33\uFF01" +
                        "  \uFF1D\uFF1D "
            )
        } else {
            print(
                "\n      \uFF1D\uFF1D  " +
                        "\uFF22\uFF2C\uFF21\uFF23\uFF2B \uFF37\uFF29\uFF2E\uFF33\uFF01" +
                        "  \uFF1D\uFF1D "
            )
        }
    }

    fun printPossibleMoves(chessPiece: EPieceType, index: Int, squares: Array<Int>, whiteTurn: Boolean) {
        if (squares.isEmpty()) {
            if (isWhite(chessPiece) == !whiteTurn) {
                println("You cannot check moves of chess pieces that belong to the opposite color!")
            } else {
                println(
                    "There are no available moves for " +
                            chessPiece.toString() + " at " + toAlgebraic(index)
                )
            }
            return
        }

        println(chessPiece.toString() + " at " + toAlgebraic(index) + " can move to the following: ")
        for (i in squares.indices) {
            print("-> " + toAlgebraic(squares[i]) + "\n")
        }
        print("\n")
    }
}