package chessPieceImplementation

import chess.utils.omniDirectional
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece
import chessStateManagement.GameManager

class Queen(gm: GameManager, piece: EPieceType) : ChessPiece(gm, piece, omniDirectional)