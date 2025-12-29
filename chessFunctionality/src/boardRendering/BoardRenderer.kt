package boardRendering

import chess.utils.getBit
import chess.utils.getBoardIndices
import chess.utils.printBitDebug
import chessData.EPieceType
import chessStateManager.GameManager

class BoardRenderer(val gm: GameManager) {
    val pieceIcons = arrayOf(
        "[\u2657]", "[\u2654]", "[\u2658]", "[\u2659]", "[\u2655]", "[\u2656]",
        "[\u265D]", "[\u265A]", "[\u265E]", "[\u265F]", "[\u265B]", "[\u265C]"
    )

    val noPiece = "[\uFF1D]"
    val rank = arrayOf(
        "[\uFF18]",
        "[\uFF17]",
        "[\uFF16]",
        "[\uFF15]",
        "[\uFF14]",
        "[\uFF13]",
        "[\uFF12]",
        "[\uFF11]"
    )
    val file = " \uFFE3   [\uFF21][\uFF22][\uFF23][\uFF24][\uFF25][\uFF26][\uFF27][\uFF28]"

    fun renderBoard(wPlayerTurn: Boolean) {
        val pieceRender = assignRendering()

        print("\n")
        print("     \uFF1D\uFF1D  ")
        if (wPlayerTurn) {
            print("\uFF37\uFF28\uFF29\uFF34\uFF25")
        } else {
            print("\uFF22\uFF2C\uFF21\uFF23\uFF2B")
        }
        print("\uFF07\uFF33  \uFF34\uFF35\uFF32\uFF2E  \uFF1D\uFF1D")
        var rankIndex = 0
        for (i in pieceRender.indices) {
                print("\n" + rank[rankIndex] + "  " + pieceRender[i])
                rankIndex++
        }
        println("\n" + file)
        println("\n")

    }

    var once = false
    fun assignRendering(): Array<String> {
        val board = gm.bsm.getBoardState()
        val pieceBoards = gm.bsm.getPieceBoards()
        val indices = getBoardIndices(board)
        val pieces = Array<String>(64) { noPiece }

        for (index in indices) {
            for (p in pieceBoards.indices) {
                if (getBit(pieceBoards[p], index)) {
                    pieces[index] = pieceIcons[p]
                }
            }
        }

        val pieceRender = Array<String>(8){""}
        for(i in pieceRender.indices) {
            var rank = ""
            for(j in i*8..i*8+7) {
                rank += pieces[j]
            }
            pieceRender[i] = rank
            println(pieceRender[i])
        }

        return pieceRender
    }
}

