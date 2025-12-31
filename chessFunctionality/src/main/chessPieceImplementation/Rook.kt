package chessPieceImplementation

import utils.cardinal
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece
import chessStateManagement.BoardStateManager

class Rook(bsm: BoardStateManager, piece: EPieceType) : ChessPiece(bsm, piece, cardinal)