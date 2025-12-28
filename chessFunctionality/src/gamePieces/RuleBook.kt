package gamePieces

import gameState.GameManager

class RuleBook(gameManager: GameManager) {
    val rules = mapOf(
        EPieceType.WBishop to Bishop(gameManager, EPieceType.WBishop),
        EPieceType.WKing to King(gameManager, EPieceType.WKing),
        EPieceType.WKnight to ChessPiece(gameManager, EPieceType.WKnight),
        EPieceType.WPawn to Pawn(gameManager, EPieceType.WPawn),
        EPieceType.WQueen to ChessPiece(gameManager, EPieceType.WQueen),
        EPieceType.WRook to ChessPiece(gameManager, EPieceType.WRook),
        EPieceType.BBishop to Bishop(gameManager, EPieceType.BBishop),
        EPieceType.BKing to King(gameManager, EPieceType.BKing),
        EPieceType.BKnight to ChessPiece(gameManager, EPieceType.BKnight),
        EPieceType.BPawn to Pawn(gameManager, EPieceType.BPawn),
        EPieceType.BQueen to ChessPiece(gameManager, EPieceType.BQueen),
        EPieceType.BRook to ChessPiece(gameManager, EPieceType.BRook)
    )
}