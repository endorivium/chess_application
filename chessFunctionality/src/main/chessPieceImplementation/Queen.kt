package main.chessPieceImplementation

import main.utils.omniDirectional
import main.chessData.EPieceType
import main.chessPieceImplementation.baseImplementation.ChessPiece
import main.chessStateManagement.GameManager

class Queen(gm: GameManager, piece: EPieceType) : ChessPiece(gm, piece, omniDirectional)