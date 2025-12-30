package main.chessPieceImplementation

import main.utils.cardinal
import main.chessData.EPieceType
import main.chessPieceImplementation.baseImplementation.ChessPiece
import main.chessStateManagement.GameManager

class Rook(gm: GameManager, piece: EPieceType) : ChessPiece(gm, piece, cardinal)