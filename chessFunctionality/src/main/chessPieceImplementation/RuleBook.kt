package chessPieceImplementation

import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece
import chessStateManagement.BoardStateManager
import chessStateManagement.GameManager

class RuleBook(bsm: BoardStateManager) {
    val rules = mapOf(
        EPieceType.WBishop to Bishop(EPieceType.WBishop),
        EPieceType.WKing to King(bsm, EPieceType.WKing),
        EPieceType.WKnight to Knight(bsm, EPieceType.WKnight),
        EPieceType.WPawn to Pawn(bsm, EPieceType.WPawn),
        EPieceType.WQueen to Queen(EPieceType.WQueen),
        EPieceType.WRook to Rook(EPieceType.WRook),
        EPieceType.BBishop to Bishop(EPieceType.BBishop),
        EPieceType.BKing to King(bsm, EPieceType.BKing),
        EPieceType.BKnight to Knight(bsm, EPieceType.BKnight),
        EPieceType.BPawn to Pawn(bsm, EPieceType.BPawn),
        EPieceType.BQueen to Queen(EPieceType.BQueen),
        EPieceType.BRook to Rook(EPieceType.BRook)
    )

    fun getRules(chessPiece: EPieceType): ChessPiece {
        val pieceRules = rules[chessPiece]
            ?: throw IllegalArgumentException("Chess Piece $chessPiece was not found in Rule Set!")
        return pieceRules
    }
}