package chessPieces

import chess.utils.cardinal
import chessData.EPieceType
import chessPieces.baseImplementation.ChessPiece
import chessStateManager.GameManager

class Rook(gm: GameManager, piece: EPieceType) : ChessPiece(gm, piece, cardinal)