package main.chessPieceImplementation

import main.utils.knightPattern
import main.chessData.EPieceType
import main.chessPieceImplementation.baseImplementation.SingleStep
import main.chessStateManagement.GameManager

class Knight(gm: GameManager, piece: EPieceType) : SingleStep(gm, piece, knightPattern)