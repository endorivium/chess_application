package chessPieceImplementation

import utils.cardinal
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece
import chessStateManagement.GameManager

class Rook(gm: GameManager, piece: EPieceType) : ChessPiece(gm, piece, cardinal)