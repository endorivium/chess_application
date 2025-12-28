import chess.utils.empty
import chess.utils.flipBit
import chess.utils.isWithinBoard
import chess.utils.knightPattern
import chess.utils.omniDirectional
import chess.utils.toAlgebraic
import chess.utils.willFileOverflow
import gameState.ChessMove
import gameState.GameManager

val gameManager: GameManager = GameManager()
fun main() {
    gameManager.initializeGame()
//    gameManager.startGameLoop()

    for (index in 0..63) {
        println("Index: " + toAlgebraic(index) + " is endangered " + willMoveCheck(index, true))
    }
}