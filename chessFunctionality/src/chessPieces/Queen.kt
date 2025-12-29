package chessPieces

import chess.utils.omniDirectional
import chessData.EPieceType
import chessPieces.baseImplementation.ChessPiece
import chessStateManager.GameManager

class Queen(gm: GameManager, piece: EPieceType) : ChessPiece(gm, piece, omniDirectional)