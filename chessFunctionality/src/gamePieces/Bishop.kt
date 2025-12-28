package gamePieces
import chess.utils.omniDiagonal
import gameState.GameManager

class Bishop(gm: GameManager, piece: EPieceType): ChessPiece(gm, piece, omniDiagonal) {}