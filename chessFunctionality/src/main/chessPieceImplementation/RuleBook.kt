package chessPieceImplementation

import chessData.EPieceType
import chessStateManagement.GameManager

class RuleBook(gm: GameManager) {
    val rules = mapOf(
        EPieceType.WBishop to Bishop(gm.getBSM(), EPieceType.WBishop),
        EPieceType.WKing to King(gm.getBSM(), EPieceType.WKing),
        EPieceType.WKnight to Knight(gm.getBSM(), EPieceType.WKnight),
        EPieceType.WPawn to Pawn(gm, gm.getBSM(), EPieceType.WPawn),
        EPieceType.WQueen to Queen(gm.getBSM(), EPieceType.WQueen),
        EPieceType.WRook to Rook(gm.getBSM(), EPieceType.WRook),
        EPieceType.BBishop to Bishop(gm.getBSM(), EPieceType.BBishop),
        EPieceType.BKing to King(gm.getBSM(), EPieceType.BKing),
        EPieceType.BKnight to Knight(gm.getBSM(), EPieceType.BKnight),
        EPieceType.BPawn to Pawn(gm,gm.getBSM(), EPieceType.BPawn),
        EPieceType.BQueen to Queen(gm.getBSM(), EPieceType.BQueen),
        EPieceType.BRook to Rook(gm.getBSM(), EPieceType.BRook)
    )
}