package chessPieceImplementation

import chess.utils.empty
import chess.utils.flipBit
import chess.utils.isWhite
import chess.utils.omniDirectional
import chessData.EPieceType
import chessData.MoveSet
import chessPieceImplementation.baseImplementation.SingleStep
import chessStateManagement.GameManager

class King(gm: GameManager, piece: EPieceType) : SingleStep(gm, piece, omniDirectional) {

    override fun getPossibleMoves(index: Int): MoveSet {
        val moves = super.getPossibleMoves(index)

        if(piece == EPieceType.BKing && gm.getBSM().bKingMoved) return moves
        if(piece == EPieceType.WKing && gm.getBSM().wKingMoved) return moves

        moves.rochade = rochadeMove(index)
        return moves
    }

    fun rochadeMove(index: Int): ULong {
        return shortRochade(index) xor longRochade(index)
    }

    fun shortRochade(index: Int): ULong {
        //check king and right rook not moved
        val kingRooksMoved = gm.getBSM().haveKingRooksMoved(isWhite(piece))
        if(kingRooksMoved.first || kingRooksMoved.third) return empty

        //check space between king and rook empty
        if(gm.getBSM().areSquaresOccupied(arrayOf(index + 1, index + 2))) return empty

        //check king space and spaces between unthreatened
        if(gm.getBSM().areSquaresThreatened(arrayOf(index, index + 1, index + 2),
                isWhite(piece))) return empty

        return flipBit(empty, index + 2)
    }

    fun longRochade(index: Int): ULong {
        //check king and right rook not moved
        val kingRooksMoved = gm.getBSM().haveKingRooksMoved(isWhite(piece))
        if(kingRooksMoved.first || kingRooksMoved.second) return empty

        //check space between king and rook empty
        if(gm.getBSM().areSquaresOccupied(arrayOf(index - 1, index - 2, index - 3))) return empty

        //check king space and spaces between unthreatened
        if(gm.getBSM().areSquaresThreatened(arrayOf(index - 1, index - 2, index - 3),
                isWhite(piece))) return empty

        return flipBit(empty, index - 2)
    }
}