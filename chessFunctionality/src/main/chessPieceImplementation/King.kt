package chessPieceImplementation

import utils.empty
import utils.flipBit
import utils.isWhite
import utils.omniDirectional
import chessData.EPieceType
import chessData.MoveSet
import chessPieceImplementation.baseImplementation.SingleStep
import chessStateManagement.BoardStateManager

class King(val bsm: BoardStateManager, piece: EPieceType) : SingleStep(piece, omniDirectional) {

    override fun calcPossibleMoves(index: Int, board: ULong, allyBoard: ULong, enemyBoard: ULong): MoveSet {
        val moves = super.calcPossibleMoves(index, board, allyBoard, enemyBoard)

        if(piece == EPieceType.BKing && bsm.bKingMoved) return moves
        if(piece == EPieceType.WKing && bsm.wKingMoved) return moves

        moves.rochade = rochadeMove(index)
        return moves
    }

    fun rochadeMove(index: Int): ULong {
        return shortRochade(index) xor longRochade(index)
    }

    fun shortRochade(index: Int): ULong {
        //check king and right rook not moved
        val kingRooksMoved = bsm.haveKingRooksMoved(isWhite(piece))
        if(kingRooksMoved.first || kingRooksMoved.third) return empty

        //check space between king and rook empty
        if(bsm.areSquaresOccupied(arrayOf(index + 1, index + 2))) return empty

        //check king space and spaces between unthreatened
        if(bsm.areSquaresThreatened(arrayOf(index, index + 1, index + 2),
                isWhite(piece))) return empty

        return flipBit(empty, index + 2)
    }

    fun longRochade(index: Int): ULong {
        //check king and right rook not moved
        val kingRooksMoved = bsm.haveKingRooksMoved(isWhite(piece))
        if(kingRooksMoved.first || kingRooksMoved.second) return empty

        //check space between king and rook empty
        if(bsm.areSquaresOccupied(arrayOf(index - 1, index - 2, index - 3))) return empty

        //check king space and spaces between unthreatened
        if(bsm.areSquaresThreatened(arrayOf(index - 1, index - 2, index - 3),
                isWhite(piece))) return empty

        return flipBit(empty, index - 2)
    }
}