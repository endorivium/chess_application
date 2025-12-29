package boardRendering

import chess.utils.getBit
import chess.utils.getBoardIndices
import chessStateManager.GameManager

class BoardRenderer(val gm: GameManager) {
    val pieceIcons = arrayOf("[\u2657]", "[\u2654]", "[\u2658]", "[\u2659]", "[\u2655]", "[\u2656]",
        "[\u265D]", "[\u265A]", "[\u265E]", "[\u265F]", "[\u265B]", "[\u265C]")
    val noPiece = "[\uFF1D]"

    var pieceRender = Array<String>(64){noPiece}
    val blank = Array<String>(64){noPiece}
    val rank = arrayOf(
        "[\uFF18]",
        "[\uFF17]",
        "[\uFF16]",
        "[\uFF15]",
        "[\uFF14]",
        "[\uFF13]",
        "[\uFF12]",
        "[\uFF11]")
    val file = " \uFFE3   [\uFF21][\uFF22][\uFF23][\uFF24][\uFF25][\uFF26][\uFF27][\uFF28]"

    fun renderBoard(wPlayerTurn: Boolean) {
        assignRendering()

        print("\n")
        print("     \uFF1D\uFF1D  ")
        if(wPlayerTurn) {
            print("\uFF37\uFF28\uFF29\uFF34\uFF25")
        } else{
            print("\uFF22\uFF2C\uFF21\uFF23\uFF2B")
        }
        print("\uFF07\uFF33  \uFF34\uFF35\uFF32\uFF2E  \uFF1D\uFF1D")
        var rankIndex = 0
        for(i in pieceRender.indices){
            if(i%8 == 0){
                print("\n" + rank[rankIndex] + "  ")
                rankIndex++
            }
            print(pieceRender[i])
        }
        println("\n" + file)
        println("\n")
    }

    fun assignRendering(){
        val board = gm.bsm.getBoardState()
        val pieceBoards = gm.bsm.getPieceBoards()

        val indices = getBoardIndices(board)
        pieceRender = blank
        for(index in indices){
            for(p in pieceBoards.indices){
                if(getBit(pieceBoards[p], index)){
                    pieceRender[index] = pieceIcons[p]
                }
            }
        }
    }
}

