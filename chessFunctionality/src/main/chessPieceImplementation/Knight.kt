package chessPieceImplementation

import utils.knightPattern
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.SingleStep
import chessStateManagement.BoardStateManager

class Knight(bsm: BoardStateManager, piece: EPieceType) : SingleStep(piece, knightPattern)