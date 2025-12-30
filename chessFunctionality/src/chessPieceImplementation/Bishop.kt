package chessPieceImplementation
import chess.utils.omniDiagonal
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece
import chessStateManagement.GameManager

class Bishop(gm: GameManager, piece: EPieceType): ChessPiece(gm, piece, omniDiagonal)