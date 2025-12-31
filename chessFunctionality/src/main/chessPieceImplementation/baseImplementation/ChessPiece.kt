package chessPieceImplementation.baseImplementation

import utils.empty
import utils.flipBit
import utils.isWithinBoard
import utils.willFileOverflow
import chessData.EMoveType
import chessData.EPieceType
import chessData.MoveSet
import chessData.ChessMove

open class ChessPiece(
    val piece: EPieceType = EPieceType.WPawn,
    val movePattern: Array<Int> = emptyArray(),
    protected var mod: Int = 0){

    init{
        mod = if(piece.ordinal in 0..5) 1 else -1
    }

    /*
    returns possible moves for given chess piece
    first ULong is normal movement, second is attack
    */
    open fun getPossibleMoves(index: Int, board: ULong, allyBoard: ULong, enemyBoard: ULong): MoveSet {
        return MoveSet(findMoves(index, board), findAttacks(index, allyBoard, enemyBoard))
    }

    open fun findMoves(index: Int, board: ULong): ULong{
        var moves = empty

        for(step in movePattern) {
            var next = index
            var move: ULong
            while (isWithinBoard(next + step)) {
                //fixes file overflow
                if (willFileOverflow(next, next + step))
                    break

                next += step
                move = flipBit(empty, next)

                if((move and board).countOneBits() != 0)
                    break

                moves = moves xor move
            }
        }
        return moves
    }

    open fun findAttacks(index: Int, allyBoard: ULong, enemyBoard: ULong): ULong{
        var attacks = empty
//        val enemyBoard = bsm.getColorBoard(!isWhite(piece))
//        val allyBoard = bsm.getColorBoard(isWhite(piece))

        for(step in movePattern){
            var next = index
            var attack: ULong
            var ally: ULong
            while(isWithinBoard(next + step)){
                //fixes file overflow
                if(willFileOverflow(next, next + step))
                    break

                next += step

                attack = flipBit(empty, next)
                ally = attack and allyBoard
                if(ally.countOneBits() != 0)
                    break

                attack = attack and enemyBoard
                if(attack.countOneBits() != 0) {
                    attacks = attacks xor attack
                    break
                }
            }
        }
        return attacks
    }

    open fun canExecuteMove(move: ChessMove, board: ULong, allyBoard: ULong, enemyBoard: ULong, simulated: Boolean = false): Pair<Boolean, EMoveType?> {
        val possibleMoves = getPossibleMoves(move.initialIndex, board, allyBoard, enemyBoard)
        val desiredMove = flipBit(empty, move.targetIndex)

        if((possibleMoves.move and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Move)
        }

        if((possibleMoves.attack and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Attack)
        }

        if((possibleMoves.rochade and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Rochade)
        }

        return Pair(false, null)
    }
}