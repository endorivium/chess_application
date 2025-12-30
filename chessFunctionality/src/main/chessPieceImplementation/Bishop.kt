package main.chessPieceImplementation
import main.utils.omniDiagonal
import main.chessData.EPieceType
import main.chessPieceImplementation.baseImplementation.ChessPiece
import main.chessStateManagement.GameManager

class Bishop(gm: GameManager, piece: EPieceType): ChessPiece(gm, piece, omniDiagonal)