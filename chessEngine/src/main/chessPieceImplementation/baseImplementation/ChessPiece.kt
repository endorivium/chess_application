package chessPieceImplementation.baseImplementation

import utils.empty
import utils.flipBit
import utils.isWithinBoard
import utils.willFileOverflow
import chessData.EMoveType
import chessData.EPieceType
import chessData.MoveSet
import chessData.ChessMove
import utils.universe

open class ChessPiece(
    val piece: EPieceType = EPieceType.WPawn,
    val movePattern: Array<Int>,
    protected var mod: Int = 0){

    init{
        mod = if(piece.ordinal in 0..5) 1 else -1
    }

    /*evaluates if the desired move can actually be executed
    rochade is for the king pieces */
    open fun canExecuteMove(move: ChessMove, board: ULong, allyBoard: ULong, enemyBoard: ULong, simulated: Boolean = false): Pair<Boolean, EMoveType?> {
        val realMoveSet = getPieceMoveSet(move.initialIndex, board, allyBoard, enemyBoard)
        if(simulated){
            realMoveSet.attack = findAllPossibleAttacks(move.initialIndex, board, allyBoard, enemyBoard)
        }
        val desiredMove = flipBit(empty, move.targetIndex)

        if((realMoveSet.move and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Move)
        }

        if((realMoveSet.attack and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Attack)
        }

        if((realMoveSet.rochade and desiredMove).countOneBits() >= 1){
            return Pair(true, EMoveType.Rochade)
        }

        return Pair(false, null)
    }

    /*returns actual possible moves and attacks that chess piece could execute*/
    open fun getPieceMoveSet(index: Int, board: ULong, allyBoard: ULong, enemyBoard: ULong): MoveSet {
        return MoveSet(findMoves(index, board), findAttacks(index, allyBoard, enemyBoard))
    }

    /*finds all squares that the piece can step onto*/
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

    /*finds all enemy occupied squares that the piece attack*/
    open fun findAttacks(index: Int, allyBoard: ULong, enemyBoard: ULong): ULong{
        var attacks = empty

        for(step in movePattern){
            var next = index
            var attack: ULong
            var ally: ULong
            while(isWithinBoard(next + step)){
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

    /*
    returns all squares that this piece could potentially reach and attack including the squares of the nearest allies
    in each direction (used for attack simulation)
     */
    open fun findAllPossibleAttacks(index: Int, board: ULong, allyBoard: ULong, enemyBoard: ULong): ULong{
        var attacks = empty

        for(step in movePattern){
            var next = index
            var attack: ULong
            while(isWithinBoard(next + step)){
                if(willFileOverflow(next, next + step))
                    break

                next += step

                attack = flipBit(empty, next) and allyBoard
                if(attack.countOneBits() != 0){
                    attacks = attacks xor attack
                    break
                }

                attack = flipBit(empty, next) and universe
                if(attack.countOneBits() != 0) {
                    attacks = attacks or attack
                }

                attack = flipBit(empty, next) and enemyBoard
                if(attack.countOneBits() != 0) {
                    attacks = attacks or attack
                    break
                }
            }
        }
        return attacks
    }
}