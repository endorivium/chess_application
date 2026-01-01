package chessPieceImplementation

import utils.omniDirectional
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece

class Queen(piece: EPieceType) : ChessPiece(piece, omniDirectional)