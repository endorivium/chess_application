package chessPieceImplementation

import utils.knightPattern
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.SingleStep
import chessStateManagement.GameManager

class Knight(gm: GameManager, piece: EPieceType) : SingleStep(gm, piece, knightPattern)