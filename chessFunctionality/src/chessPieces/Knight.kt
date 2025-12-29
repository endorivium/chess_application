package chessPieces

import chess.utils.knightPattern
import chessData.EPieceType
import chessPieces.baseImplementation.SingleStep
import chessStateManager.GameManager

class Knight(gm: GameManager, piece: EPieceType) : SingleStep(gm, piece, knightPattern)