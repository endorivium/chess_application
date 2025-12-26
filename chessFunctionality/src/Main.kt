import bitoperation.utils.printBitDebug
import gamePieces.EPieceType
import gamePieces.Pawn
import gameState.BoardStateManager

fun main() {
    //val gameManager = GameManager()
    //gameManager.startGame()
    val pawn = Pawn()
    val bSManager = BoardStateManager()

    val push = pawn.getPush(9)
    printBitDebug(push, "push")
    val boardState: ULong = bSManager.getBoardState()
    printBitDebug(boardState, "board")
    var attack = pawn.getAttack(9)
    attack = attack and bSManager.getPieceBoard(EPieceType.bPawn)
    printBitDebug(attack, "attack")

    //gets pushes that are actually possible by combining (xor) and then excluding (and)
    var forwardMoves: ULong = (push xor boardState) and push
    forwardMoves = forwardMoves xor attack
    printBitDebug(forwardMoves, "forwardMoves")
}