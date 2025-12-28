package gamePieces

import chess.utils.empty
import chess.utils.flipBit
import chess.utils.isWithinBoard
import chess.utils.knightPattern
import chess.utils.omniDirectional
import chess.utils.willFileOverflow
import gameState.ChessMove
import gameState.GameManager

class King(gm: GameManager, piece: EPieceType) : ChessPiece(gm, piece, omniDirectional) {

    override fun getPossibleMoves(posIndex: Int): MoveSet {
        val moves = super.getPossibleMoves(posIndex)
        //TODO: add rochade here
        moves.movement = moves.movement xor rochadeMove(posIndex)
        return moves
    }


    override fun findMoves(posIndex: Int): ULong {
        var moves = empty
        val board = gm.bSManager.getBoardState()

        for (step in movePattern) {
            var next = posIndex
            //fixes file overflow
            if (willFileOverflow(next, next + step))
                continue

            next += step
            val move: ULong = flipBit(empty, next)

            if ((move and board).countOneBits() != 0)
                continue

            moves = moves xor move
        }
        return moves
    }

    override fun findAttacks(posIndex: Int): ULong {
        var attacks = empty
        val board = gm.bSManager.getBoardState()

        for (step in movePattern) {
            var next = posIndex
            //fixes file overflow
            if (willFileOverflow(next, next + step))
                continue

            next += step

            var attack: ULong = flipBit(empty, next)
            attack = attack and board
            if (attack.countOneBits() != 0
                && isEnemy(gm.bSManager.getPieceAt(next)!!)
            ) {
                attacks = attacks xor attack
            }
        }
        return attacks
    }


    fun rochadeMove(index: Int): ULong {
        //TODO: calculate rochade
        return empty
    }

    fun canRochade(): Boolean {
        //TODO: do all checks
        return false
    }

    fun willMoveCheck(index: Int, isWPlayer: Boolean): Boolean {
        val possibleAtks = findAttackers(index, isWPlayer)

        return possibleAtks.countOneBits() != 0
    }

    fun findAttackers(index: Int, isWPlayer: Boolean): ULong {
        var possibleAtks: ULong = empty
        val enemyBoard = gm.bSManager.getEnemyBoard(isWPlayer)
        for (step in omniDirectional) {
            var next = index
            var enemy: ULong
            while (isWithinBoard(next)) {
                //fixes file overflow
                if (willFileOverflow(next, next + step))
                    break

                next += step

                enemy = flipBit(empty, next)
                enemy = enemy and enemyBoard
                if (enemy.countOneBits() != 0) {
                    possibleAtks = possibleAtks xor simulateAtk(next, index)
                }
            }
        }

        possibleAtks = possibleAtks xor findKnightAttacks(index, enemyBoard)

        return possibleAtks
    }

    fun findKnightAttacks(index: Int, enemyBoard: ULong): ULong {
        var knights = empty
        for (jump in knightPattern) {
            if (willFileOverflow(index, index + jump)
                || !isWithinBoard(index + jump)
            )
                break

            var knight = flipBit(empty, index + jump)
            knight = knight and enemyBoard
            if (knight.countOneBits() != 0) {
                knights = knights xor knight
            }
        }
        return knights
    }

    fun simulateAtk(attacker: Int, target: Int): ULong {
        val simulated = ChessMove(initialIndex = attacker, targetIndex = target)
        val piece = gm.bSManager.getPieceAt(attacker)
            ?: IllegalArgumentException("Attacker should not be null!")

        val pieceRule = gm.ruleBook.rules[piece]
            ?: throw IllegalStateException("Chess Piece ($piece) was not found in Rule Set!")

        if (pieceRule.canExecuteMove(simulated).first) {
            return flipBit(empty, attacker)
        }
        return empty
    }
}