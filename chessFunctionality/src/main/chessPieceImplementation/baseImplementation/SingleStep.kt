package chessPieceImplementation.baseImplementation

import utils.empty
import utils.flipBit
import utils.isWhite
import utils.isWithinBoard
import utils.willFileOverflow
import chessData.EPieceType
import chessStateManagement.GameManager

open class SingleStep(
    gm: GameManager,
    piece: EPieceType,
    movePattern: Array<Int>
) :
    ChessPiece(gm, piece, movePattern) {

    override fun findMoves(index: Int): ULong {
        var moves = empty
        val board = gm.getBSM().getBoardState()

        for (step in movePattern) {
            if (willFileOverflow(index, index + step)
                || !isWithinBoard(index + step))
                continue

            val move: ULong = flipBit(empty, index + step)

            if ((move and board).countOneBits() != 0)
                continue

            moves = moves xor move
        }
        return moves
    }

    override fun findAttacks(index: Int): ULong {
        var attacks = empty
        val enemyBoard = gm.getBSM().getColorBoard(!isWhite(piece))
        val allyBoard = gm.getBSM().getColorBoard(isWhite(piece))

        for (step in movePattern) {
            if (willFileOverflow(index, index + step)
                || !isWithinBoard(index + step))
                continue

            var attack = flipBit(empty, index + step)
            val ally = attack and allyBoard
            if(ally.countOneBits() != 0)
                continue

            attack = attack and enemyBoard
            if(attack.countOneBits() != 0) {
                attacks = attacks xor attack
            }
        }
        return attacks
    }
}