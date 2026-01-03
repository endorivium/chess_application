package chessPieceImplementation

import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece
import chessPieceImplementation.baseImplementation.SingleStep
import chessStateManagement.BoardStateManager
import utils.cardinal
import utils.knightPattern
import utils.omniDiagonal
import utils.omniDirectional

class RuleBook(bsm: BoardStateManager) {
    val rules = mapOf(
        EPieceType.WBishop to ChessPiece(EPieceType.WBishop, omniDiagonal),
        EPieceType.WKing to King(bsm, EPieceType.WKing),
        EPieceType.WKnight to SingleStep(EPieceType.WKnight, knightPattern),
        EPieceType.WPawn to Pawn(bsm, EPieceType.WPawn),
        EPieceType.WQueen to ChessPiece(EPieceType.WQueen, omniDirectional),
        EPieceType.WRook to ChessPiece(EPieceType.WRook, cardinal),
        EPieceType.BBishop to ChessPiece(EPieceType.BBishop, omniDiagonal),
        EPieceType.BKing to King(bsm, EPieceType.BKing),
        EPieceType.BKnight to SingleStep(EPieceType.BKnight, knightPattern),
        EPieceType.BPawn to Pawn(bsm, EPieceType.BPawn),
        EPieceType.BQueen to ChessPiece(EPieceType.BQueen, omniDirectional),
        EPieceType.BRook to ChessPiece(EPieceType.BRook, cardinal),
    )

    fun getRules(chessPiece: EPieceType): ChessPiece {
        val pieceRules = rules[chessPiece]
            ?: throw IllegalArgumentException("Chess Piece $chessPiece was not found in Rule Set!")
        return pieceRules
    }
}