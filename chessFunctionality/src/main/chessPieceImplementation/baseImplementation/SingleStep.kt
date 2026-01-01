package chessPieceImplementation.baseImplementation

import utils.empty
import utils.flipBit
import utils.isWithinBoard
import utils.willFileOverflow
import chessData.EPieceType
import utils.universe

open class SingleStep(
    piece: EPieceType,
    movePattern: Array<Int>
) :
    ChessPiece(piece, movePattern) {

    override fun findMoves(index: Int, board: ULong): ULong {
        var moves = empty

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

    override fun findAttacks(index: Int, allyBoard: ULong, enemyBoard: ULong): ULong {
        var attacks = empty

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

    override fun findAllPossibleAttacks(index: Int, board: ULong, allyBoard: ULong, enemyBoard: ULong): ULong {
        var attacks = empty

        for (step in movePattern) {
            if (willFileOverflow(index, index + step)
                || !isWithinBoard(index + step))
                continue

            var attack: ULong = flipBit(empty, index + step) and allyBoard
            if(attack.countOneBits() != 0){
                attacks = attacks xor attack
                continue
            }

            attack = flipBit(empty, index + step) and universe
            if(attack.countOneBits() != 0) {
                attacks = attacks or attack
            }

            attack = flipBit(empty, index + step) and enemyBoard
            if(attack.countOneBits() != 0) {
                attacks = attacks or attack
                continue
            }
        }
        return attacks
        }
    }
