package chessPieces

import chessData.EPieceType
import chessStateManager.GameManager

class RuleBook(gameManager: GameManager) {
    val rules = mapOf(
        EPieceType.WBishop to Bishop(gameManager, EPieceType.WBishop),
        EPieceType.WKing to King(gameManager, EPieceType.WKing),
        EPieceType.WKnight to Knight(gameManager, EPieceType.WKnight),
        EPieceType.WPawn to Pawn(gameManager, EPieceType.WPawn),
        EPieceType.WQueen to Queen(gameManager, EPieceType.WQueen),
        EPieceType.WRook to Rook(gameManager, EPieceType.WRook),
        EPieceType.BBishop to Bishop(gameManager, EPieceType.BBishop),
        EPieceType.BKing to King(gameManager, EPieceType.BKing),
        EPieceType.BKnight to Knight(gameManager, EPieceType.BKnight),
        EPieceType.BPawn to Pawn(gameManager, EPieceType.BPawn),
        EPieceType.BQueen to Queen(gameManager, EPieceType.BQueen),
        EPieceType.BRook to Rook(gameManager, EPieceType.BRook)
    )
}