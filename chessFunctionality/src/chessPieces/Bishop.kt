package chessPieces
import chess.utils.omniDiagonal
import chessData.EPieceType
import chessPieces.baseImplementation.ChessPiece
import chessStateManager.GameManager

class Bishop(gm: GameManager, piece: EPieceType): ChessPiece(gm, piece, omniDiagonal) {}