package boardRendering

class CmdBoardRenderer: BoardRenderer() {
    override fun initialize() {
        pieceIcons = arrayOf(
            "[wBi]", "[wKi]", "[wKn]", "[wPa]", "[wQu]", "[wRo]",
            "[bBi]", "[bKi]", "[bKn]", "[bPa]", "[bQu]", "[bRo]"
        )

        noPiece = "[===]"
        rank = arrayOf(
            "[8]",
            "[7]",
            "[6]",
            "[5]",
            "[4]",
            "[3]",
            "[2]",
            "[1]"
        )

        file = "     [ A ][ B ][ C ][ D ][ E ][ F ][ G ][ H ]"
    }

    override fun renderBoard(
        whiteTurn: Boolean, check: Boolean, checkMate: Boolean,
        board: ULong, pieceBoards: Array<ULong>
    ) {
        val boardRender = refreshRendering(board, pieceBoards)

        print("\n")
        print("    =============  ")
        if (whiteTurn) {
            print("WHITE")
        } else {
            print("BLACK")
        }
        print("'S TURN  =============\n")
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

    override fun printCheck() {
        print(
            "\n    =============  " +
                    "KING IN CHECK" +
                    "  ============="
        )
    }

    override fun printCheckMate(whiteTurn: Boolean) {
        print(
            "\n    =============  " +
                    "CHECKMATE!" +
                    "  =============\n"
        )
        if (!whiteTurn) {
            print(
                "    =============  " +
                        "BLACK WINS!" +
                        "  ============="
            )
        } else {
            print(
                "    =============  " +
                        "WHITE WINS!" +
                        "  ============="
            )
        }
    }
}