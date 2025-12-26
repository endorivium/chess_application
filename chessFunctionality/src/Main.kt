import bitoperation.utils.printBitDebug
import gamePieces.EPieceType
import gamePieces.Pawn
import gameState.BoardStateManager

fun main() {
    //val gameManager = GameManager()
    //gameManager.startGame()

    val pawn = Pawn(EPieceType.WPawn)
    val bSManager = BoardStateManager()

    val push = pawn.getPush(52)
    val boardState: ULong = bSManager.getBoardState()
    var attack = pawn.getAttack(52)
    attack = attack and bSManager.getEnemyPieceBoard(pawn.piece)

    //gets pushes that are actually possible by combining (xor) and then excluding (and)
    var forwardMoves: ULong = (push xor boardState) and push
    forwardMoves = forwardMoves xor attack
    printBitDebug(forwardMoves, "pawn moves:")

    printBitDebug(pawn.getPossibleMoves(52), "possible pawn moves (function call):")
}