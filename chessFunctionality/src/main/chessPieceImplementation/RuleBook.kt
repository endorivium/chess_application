package chessPieceImplementation

import chessData.EPieceType
import chessStateManagement.BoardStateManager
import chessStateManagement.GameManager

class RuleBook(bsm: BoardStateManager) {
    val rules = mapOf(
        EPieceType.WBishop to Bishop(bsm, EPieceType.WBishop),
        EPieceType.WKing to King(bsm, EPieceType.WKing),
        EPieceType.WKnight to Knight(bsm, EPieceType.WKnight),
        EPieceType.WPawn to Pawn(bsm, EPieceType.WPawn),
        EPieceType.WQueen to Queen(bsm, EPieceType.WQueen),
        EPieceType.WRook to Rook(bsm, EPieceType.WRook),
        EPieceType.BBishop to Bishop(bsm, EPieceType.BBishop),
        EPieceType.BKing to King(bsm, EPieceType.BKing),
        EPieceType.BKnight to Knight(bsm, EPieceType.BKnight),
        EPieceType.BPawn to Pawn(bsm, EPieceType.BPawn),
        EPieceType.BQueen to Queen(bsm, EPieceType.BQueen),
        EPieceType.BRook to Rook(bsm, EPieceType.BRook)
    )
}