package gamePieces

import chess.utils.empty
import chess.utils.flipBit
import chess.utils.isWPlayer
import chess.utils.isWithinBoard
import chess.utils.willFileOverflow
import gameState.ChessMove
import gameState.GameManager

open class ChessPiece(
    val gm: GameManager,
    val piece: EPieceType = EPieceType.WPawn,
    val movePattern: Array<Int> = emptyArray(),
    var mod: Int = 0){

    init{
        mod = if(piece.ordinal in 0..5) 1 else -1
    }

    /*
    returns possible moves for given chess piece
    first ULong is normal movement, second is attack
    */
    open fun getPossibleMoves(posIndex: Int): MoveSet {
        val enemyBoard = gm.bSManager.getEnemyBoard(isWPlayer(piece))
        var move = empty
        var attack = empty

        for(step in movePattern){
            var next = posIndex
            var singleAtk = empty
            while(isWithinBoard(next)){
                //fixes file overflow
                if(willFileOverflow(next, next + step))
                    break

                next += step

                singleAtk = flipBit(empty, next)
                singleAtk = singleAtk and enemyBoard
                if(singleAtk.countOneBits() == 0) {
                    attack = attack xor singleAtk
                }
                move = flipBit(move, next)
            }
        }
        move = (move and gm.bSManager.getBoardState()) xor move
        return MoveSet(move, attack)
    }
    fun canExecuteMove(move: ChessMove): Pair<Boolean, EMoveType?> {
        val possibleMoves = getPossibleMoves(move.initialIndex)
        val desiredMove = flipBit(empty, move.targetIndex)

        if((possibleMoves.movement and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Push)
        }

        if((possibleMoves.attack and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Attack)
        }

        return Pair(false, null)
    }

    fun isEnemy(other: EPieceType): Boolean {
        return piece.ordinal in 0..5 && other.ordinal !in 0..5
    }
}