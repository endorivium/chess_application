package chessPieceImplementation

import utils.knightPattern
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.SingleStep

class Knight(piece: EPieceType) : SingleStep(piece, knightPattern)