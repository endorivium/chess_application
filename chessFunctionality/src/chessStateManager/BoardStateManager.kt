package chessStateManager

import chess.utils.empty
import chess.utils.flipBit
import chess.utils.getBoardIndices
import chess.utils.isWPlayer
import chess.utils.isWithinBoard
import chess.utils.knightPattern
import chess.utils.omniDirectional
import chess.utils.swapBit
import chess.utils.willFileOverflow
import chessData.ChessMove
import chessData.EMoveType
import chessData.EPieceType


class BoardStateManager() {
    //region GameStateVariables
    var wKingMoved = false
    var wLeftRookMoved = false
    var wRightRookMoved = false

    var bKingMoved = false
    var bLeftRookMoved = false
    var bRightRookMoved = false
    var execRochade = false
    //endregion

    //region BlackBoard
    var wBishopBoard: ULong = 0x2400000000000000u
    var wKingBoard: ULong = 0x800000000000000u
    var wKnightBoard: ULong = 0x4200000000000000u
    var wPawnBoard: ULong = 0xFF000000000000u
    var wQueenBoard: ULong = 0x1000000000000000u
    var wRookBoard: ULong = 0x8100000000000000u
    //endregion

    //region WhiteBoard
    var bBishopBoard: ULong = 0x24u
    var bKingBoard: ULong = 0x8u
    var bKnightBoard: ULong = 0x42u
    var bPawnBoard: ULong = 0xFF00u
    var bQueenBoard: ULong = 0x10u
    var bRookBoard: ULong = 0x81u
    //endregion

    var boards = arrayOf(
        wBishopBoard,
        wKingBoard,
        wKnightBoard,
        wPawnBoard,
        wQueenBoard,
        wRookBoard,
        bBishopBoard,
        bKingBoard,
        bKnightBoard,
        bPawnBoard,
        bQueenBoard,
        bRookBoard
    )

    lateinit var gm: GameManager

    fun setGameManager(gameManager: GameManager) {
        gm = gameManager
    }

    fun findPossibleMoves(move: ChessMove): Pair<Boolean, MutableList<Int>> {
        val chessPiece: EPieceType = getPieceAt(move.initialIndex)
            ?: return Pair(false, mutableListOf())

        val pieceRule = gm.ruleBook.rules[chessPiece]
            ?: throw IllegalStateException("Chess Piece ($chessPiece) was not found in Rule Set!")

        val possibleMoves = pieceRule.getPossibleMoves(move.initialIndex)

        return Pair(true, getBoardIndices(possibleMoves.move xor possibleMoves.attack))
    }

    fun execChessMove(move: ChessMove): Boolean {
        val chessPiece: EPieceType = getPieceAt(move.initialIndex)
            ?: return false

        val pieceRule = gm.ruleBook.rules[chessPiece]
            ?: throw IllegalStateException("Chess Piece ($chessPiece) was not found in Rule Set!")

        val moveExec = pieceRule.canExecuteMove(move)
        if (moveExec.first) {
            move(moveExec.second!!, chessPiece, move)
            return true
        }
        return false
    }

    fun move(type: EMoveType, piece: EPieceType, move: ChessMove) {
        when (type) {
            EMoveType.Push -> execPush(piece, move)
            EMoveType.Attack -> execAttack(piece, move)
            else -> execRochade(piece, move)
        }
    }

    fun execPush(piece: EPieceType, move: ChessMove) {
        boards[piece.ordinal] = swapBit(boards[piece.ordinal], move.initialIndex, move.targetIndex)
    }

    fun execAttack(piece: EPieceType, move: ChessMove) {
        val enemy = getPieceAt(move.targetIndex)
            ?: throw IllegalStateException("There was no enemy to attack at " + move.targetIndex)

        boards[piece.ordinal] = swapBit(boards[piece.ordinal], move.initialIndex, move.targetIndex)
        boards[enemy.ordinal] = flipBit(boards[enemy.ordinal], move.targetIndex)
    }

    fun execRochade(piece: EPieceType, move: ChessMove) {
        boards[piece.ordinal] = swapBit(boards[piece.ordinal], move.initialIndex, move.targetIndex)

        //short rochade (moving right)
        if(move.initialIndex < move.targetIndex){
            if(isWPlayer(piece)){
                boards[5] = swapBit(boards[5], move.initialIndex + 3, move.targetIndex - 1)
            } else {
                boards[11] = swapBit(boards[11], move.initialIndex + 3, move.targetIndex - 1)
            }
        } else { //long rochade (moving left)
            if(isWPlayer(piece)){
                boards[5] = swapBit(boards[5], move.initialIndex - 4, move.targetIndex + 1)
            } else {
                boards[11] = swapBit(boards[11], move.initialIndex - 4, move.targetIndex + 1)
            }
        }
    }

    fun getPieceAt(pieceIndex: Int): EPieceType? {
        val pieceBit = flipBit(empty, pieceIndex)

        for (i in boards.indices) {
            if ((pieceBit and boards[i]).countOneBits() >= 1) {
                return EPieceType.fromInt(i)
            }
        }
        return null
    }

    fun getPieceBoard(piece: EPieceType): ULong {
        if (piece.ordinal !in boards.indices) {
            throw IllegalStateException("Piece $piece was not found in Board Set!")
        }

        return boards[piece.ordinal]
    }

    fun getEnemyBoard(wPlayer: Boolean): ULong {
        var enemyBoard: ULong = empty
        if (wPlayer) {
            for (i in 6..11) {
                enemyBoard = enemyBoard xor getPieceBoard(EPieceType.fromInt(i)!!)
            }
        } else {
            for (i in 0..5) {
                enemyBoard = enemyBoard xor getPieceBoard(EPieceType.fromInt(i)!!)
            }
        }
        return enemyBoard
    }

    fun getPieceBoards(): Array<ULong> {
        val pieceBoards = Array(12) { empty }

        for (i in pieceBoards.indices) {
            pieceBoards[i] = boards[i]
        }

        return pieceBoards
    }

    fun getBoardState(): ULong {
        var board: ULong = empty

        for (piece in boards) {
            board = board xor piece
        }
        return board
    }

    fun isSquareUnoccupied(index: Int): Boolean {
        val square = flipBit(empty, index)
        val occupied = square and getBoardState()
        return occupied.countOneBits() != 0
    }

    fun areSquaresUnoccupied(indices: Array<Int>): Boolean {
        for (index in indices) {
            if (!isSquareUnoccupied(index)) return false
        }
        return true
    }

    fun isSquareThreatened(index: Int, isWPlayer: Boolean): Boolean {
        val possibleAtks = findAttackers(index, isWPlayer)

        return possibleAtks.countOneBits() != 0
    }

    fun areSquaresThreatened(indices: Array<Int>, isWPlayer: Boolean): Boolean {
        for (index in indices) {
            if (isSquareThreatened(index, isWPlayer)) return true
        }
        return false
    }

    fun findAttackers(index: Int, isWPlayer: Boolean): ULong {
        var possibleAtks: ULong = empty
        val enemyBoard = getEnemyBoard(isWPlayer)
        for (step in omniDirectional) {
            var next = index
            var enemy: ULong
            while (isWithinBoard(next)) {
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
        val piece = getPieceAt(attacker)
            ?: IllegalArgumentException("Attacker should not be null!")

        val pieceRule = gm.ruleBook.rules[piece]
            ?: throw IllegalStateException("Chess Piece ($piece) was not found in Rule Set!")

        if (pieceRule.canExecuteMove(simulated).first) {
            return flipBit(empty, attacker)
        }
        return empty
    }

    fun haveKingRooksMoved(isWPlayer: Boolean): Triple<Boolean, Boolean, Boolean> {
        if (isWPlayer)
            return Triple(wKingMoved, wLeftRookMoved, wRightRookMoved)
        return Triple(bKingMoved, bLeftRookMoved, bRightRookMoved)
    }
}