import bitoperation.utils.printBitDebug
import gamePieces.EPieceType
import gamePieces.Pawn
import gameState.BoardStateManager
import gameState.GameManager

fun main() {
    val gameManager = GameManager()
    gameManager.initializeGame()
    gameManager.startGameLoop()
}