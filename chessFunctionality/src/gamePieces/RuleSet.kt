package gamePieces

import gameState.GameManager

class RuleSet(gameManager: GameManager) {
    val rules = mapOf(
        EPieceType.WBishop to ChessPiece(gameManager, EPieceType.WBishop),
        EPieceType.WKing to ChessPiece(gameManager, EPieceType.WKing),
        EPieceType.WKnight to ChessPiece(gameManager, EPieceType.WKnight),
        EPieceType.WPawn to Pawn(gameManager, EPieceType.WPawn),
        EPieceType.WQueen to ChessPiece(gameManager, EPieceType.WQueen),
        EPieceType.WRook to ChessPiece(gameManager, EPieceType.WRook),
        EPieceType.BBishop to ChessPiece(gameManager, EPieceType.BBishop),
        EPieceType.BKing to ChessPiece(gameManager, EPieceType.BKing),
        EPieceType.BKnight to ChessPiece(gameManager, EPieceType.BKnight),
        EPieceType.BPawn to Pawn(gameManager, EPieceType.BPawn),
        EPieceType.BQueen to ChessPiece(gameManager, EPieceType.BQueen),
        EPieceType.BRook to ChessPiece(gameManager, EPieceType.BRook)
    )
}