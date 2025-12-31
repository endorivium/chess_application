package chessPieceImplementation
import utils.omniDiagonal
import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece
import chessStateManagement.BoardStateManager
import chessStateManagement.GameManager

class Bishop(bsm: BoardStateManager, piece: EPieceType): ChessPiece(bsm, piece, omniDiagonal)