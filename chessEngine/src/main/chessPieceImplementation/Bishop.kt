package chessPieceImplementation
import utils.omniDiagonal
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece

class Bishop(piece: EPieceType): ChessPiece(piece, omniDiagonal)