package chessPieces.baseImplementation

import chess.utils.empty
import chess.utils.flipBit
import chess.utils.willFileOverflow
import chessData.EPieceType
import chessStateManager.GameManager

open class SingleStep(
    gm: GameManager,
    piece: EPieceType,
    movePattern: Array<Int>
) :
    ChessPiece(gm, piece, movePattern) {

    override fun findMoves(index: Int): ULong {
        var moves = empty
        val board = gm.bsm.getBoardState()

        for (step in movePattern) {
            //fixes file overflow
            if (willFileOverflow(index, index + step))
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
        val board = gm.bsm.getBoardState()

        for (step in movePattern) {
            //fixes file overflow
            if (willFileOverflow(index, index + step))
                continue

            var attack: ULong = flipBit(empty, index)
            attack = attack and board
            if (attack.countOneBits() != 0
                && isEnemy(gm.bsm.getPieceAt(index)!!)
            ) {
                attacks = attacks xor attack
            }
        }
        return attacks
    }
}