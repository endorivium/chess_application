import chessStateManager.GameManager
import java.lang.Character.toChars

val gameManager: GameManager = GameManager()
fun main() {
    gameManager.initializeGame()
    gameManager.startGameLoop()
}