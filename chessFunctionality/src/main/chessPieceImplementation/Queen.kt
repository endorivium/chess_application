package chessPieceImplementation

import utils.omniDirectional
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece
import chessStateManagement.BoardStateManager

class Queen(bsm: BoardStateManager, piece: EPieceType) : ChessPiece(bsm, piece, omniDirectional)