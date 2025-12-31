package chessStateManagement

import utils.empty
import utils.flipBit
import utils.flipBits
import utils.getBoardIndices
import utils.isWhite
import utils.isWithinBoard
import utils.knightPattern
import utils.omniDirectional
import utils.swapBit
import utils.willFileOverflow
import chessData.ChessMove
import chessData.EMoveType
import chessData.EPieceType


open class BoardStateManager(
    private val gm: GameManager,
    val moveHistory: MutableList<ChessMove> = mutableListOf()
) {
    //region GameStateVariables
    var wKingMoved = false
    var wLeftRookMoved = false
    var wRightRookMoved = false

    var bKingMoved = false
    var bLeftRookMoved = false
    var bRightRookMoved = false
    //endregion

    //region BlackBoard
    private var wBishopBoard: ULong = 0x2400000000000000u
    private var wKingBoard: ULong = 0x800000000000000u
    private var wKnightBoard: ULong = 0x4200000000000000u
    private var wPawnBoard: ULong = 0xFF000000000000u
    private var wQueenBoard: ULong = 0x1000000000000000u
    private var wRookBoard: ULong = 0x8100000000000000u
    //endregion

    //region WhiteBoard
    private var bBishopBoard: ULong = 0x24u
    private var bKingBoard: ULong = 0x8u
    private var bKnightBoard: ULong = 0x42u
    private var bPawnBoard: ULong = 0xFF00u
    private var bQueenBoard: ULong = 0x10u
    private var bRookBoard: ULong = 0x81u
    //endregion

    private var boards = arrayOf(
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

    fun initialize() {
        boards = arrayOf(
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
        wKingMoved = false
        wLeftRookMoved = false
        wRightRookMoved = false
        bKingMoved = false
        bLeftRookMoved = false
        bRightRookMoved = false
    }

    fun findPossibleMoves(move: ChessMove, whiteTurn: Boolean): Pair<Boolean, MutableList<Int>> {
        val chessPiece: EPieceType = getPieceAt(move.initialIndex, whiteTurn)
            ?: return Pair(false, mutableListOf())

        val pieceRule = gm.getRules(chessPiece)

        val possibleMoves = pieceRule.getPossibleMoves(
            move.initialIndex, getBoardState(),
            getColorBoard(whiteTurn), getColorBoard(!whiteTurn))

        return Pair(true, getBoardIndices(possibleMoves.move))
    }

    fun execChessMove(move: ChessMove, whiteTurn: Boolean): Boolean {
        val chessPiece: EPieceType = getPieceAt(move.initialIndex, whiteTurn)
            ?: return false

        val pieceRule = gm.getRules(chessPiece)

        val moveExec = pieceRule.canExecuteMove(move, getBoardState(),
            getColorBoard(whiteTurn), getColorBoard(!whiteTurn))
        if (moveExec.first) {
            when (moveExec.second!!) {
                EMoveType.Push -> execPush(chessPiece, move)
                EMoveType.Attack -> execAttack(chessPiece, move, whiteTurn)
                else -> execRochade(chessPiece, move)
            }

            checkKingRooksMoved(chessPiece, move.initialIndex)
            return true
        }
        return false
    }

    private fun execPush(piece: EPieceType, move: ChessMove) {
        boards[piece.ordinal] = swapBit(boards[piece.ordinal], move.initialIndex, move.targetIndex)
    }

    private fun execAttack(piece: EPieceType, move: ChessMove, whiteTurn: Boolean) {
        val enemy = getPieceAt(move.targetIndex, !whiteTurn)
            ?: throw IllegalStateException("There was no enemy to attack at " + move.targetIndex)

        boards[piece.ordinal] = swapBit(boards[piece.ordinal], move.initialIndex, move.targetIndex)
        boards[enemy.ordinal] = flipBit(boards[enemy.ordinal], move.targetIndex)
    }

    private fun execRochade(piece: EPieceType, move: ChessMove) {
        boards[piece.ordinal] = swapBit(boards[piece.ordinal], move.initialIndex, move.targetIndex)

        //short rochade (moving right)
        if (move.initialIndex < move.targetIndex) {
            if (isWhite(piece)) {
                boards[5] = swapBit(boards[5], move.initialIndex + 3, move.targetIndex - 1)
            } else {
                boards[11] = swapBit(boards[11], move.initialIndex + 3, move.targetIndex - 1)
            }
        } else { //long rochade (moving left)
            if (isWhite(piece)) {
                boards[5] = swapBit(boards[5], move.initialIndex - 4, move.targetIndex + 1)
            } else {
                boards[11] = swapBit(boards[11], move.initialIndex - 4, move.targetIndex + 1)
            }
        }
    }

    fun getPieceAt(pieceIndex: Int, whiteTurn: Boolean): EPieceType? {
        val pieceBit = flipBit(empty, pieceIndex)

        for (i in boards.indices) {
            if ((pieceBit and boards[i]).countOneBits() >= 1) {
                if (isWhite(EPieceType.fromInt(i)!!) == whiteTurn)
                    return EPieceType.fromInt(i)
            }
        }
        return null
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
        if (piece.ordinal !in boards.indices)
            throw IllegalStateException("$piece was not found in Board Set!")

        return boards[piece.ordinal]
    }

    fun getPieceBoard(index: Int): ULong {
        if (index !in boards.indices)
            throw IllegalArgumentException("$index was out of bounds for boards!")

        return boards[index]
    }

    fun getColorBoard(white: Boolean): ULong {
        var enemyBoard: ULong = empty
        if (white) {
            for (i in 0..5) {
                enemyBoard = enemyBoard xor getPieceBoard(EPieceType.fromInt(i)!!)
            }
        } else {
            for (i in 6..11) {
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


    fun areSquaresOccupied(indices: Array<Int>): Boolean {
        for (index in indices) {
            val square = flipBit(empty, index)
            val occupied = square and getBoardState()
            if (occupied.countOneBits() != 0) return true
        }
        return false
    }

    fun isSquareThreatened(index: Int, isWPlayer: Boolean): Boolean {
        val enemyBoard = getColorBoard(!isWPlayer)

        //walk in all directions until enemy then check if it can attack square at index
        for (step in omniDirectional) {
            var next = index
            var enemy: ULong
            while (isWithinBoard(next + step)) {
                if (willFileOverflow(next, next + step))
                    break

                next += step

                enemy = flipBit(empty, next)
                enemy = enemy and enemyBoard
                if (enemy.countOneBits() != 0 && simulateAtk(next, index)) {
                    return true
                }
            }
        }

        //check if knights can attack square
        for (step in knightPattern) {
            if (willFileOverflow(index, index + step)
                || !isWithinBoard(index + step)
            )
                continue
            var knight = flipBit(empty, index + step)
            knight = knight and enemyBoard
            if (knight.countOneBits() != 0) {
                return true
            }
        }

        return false
    }

    fun areSquaresThreatened(indices: Array<Int>, isWPlayer: Boolean): Boolean {
        for (index in indices) {
            if (isSquareThreatened(index, isWPlayer)) return true
        }
        return false
    }

    fun getThreatenedSquares(indices: Array<Int>, isWPlayer: Boolean): ULong {
        var threatened: ULong = empty
        for (index in indices) {
            if (isSquareThreatened(index, isWPlayer)) {
                threatened = threatened xor flipBit(empty, index)
            }
        }
        return threatened
    }

    private fun simulateAtk(attacker: Int, target: Int): Boolean {
        val simulated = ChessMove(initialIndex = attacker, targetIndex = target)
        val chessPiece: EPieceType = getPieceAt(attacker)
            ?: throw IllegalArgumentException("Attacker should not be null!")

        val pieceRule = gm.getRules(chessPiece)

        val white = isWhite(chessPiece)
        return pieceRule.canExecuteMove(simulated, getBoardState(),
            getColorBoard(white), getColorBoard(!white)).first
    }

    fun haveKingRooksMoved(isWPlayer: Boolean): Triple<Boolean, Boolean, Boolean> {
        if (isWPlayer)
            return Triple(wKingMoved, wLeftRookMoved, wRightRookMoved)
        return Triple(bKingMoved, bLeftRookMoved, bRightRookMoved)
    }

    private fun checkKingRooksMoved(piece: EPieceType, index: Int) {
        when (piece) {
            EPieceType.BKing -> bKingMoved = true
            EPieceType.WKing -> wKingMoved = true
            EPieceType.BRook -> if (index == 0) bLeftRookMoved = true
            else if (index == 7) bRightRookMoved = true

            EPieceType.WRook -> if (index == 56) wLeftRookMoved = true
            else if (index == 63) wRightRookMoved = true

            else -> return
        }
    }

    fun isCheck(whiteTurn: Boolean): Boolean {
        val kingBoard = getPieceBoard(if (whiteTurn) 1 else 7)
        val kingIndex = getBoardIndices(kingBoard)

        if (kingIndex.isEmpty() || kingIndex.size != 1)
            throw IllegalStateException("There was either no or more than one King on the board!")

        val threatened = isSquareThreatened(kingIndex[0], whiteTurn)
        return threatened
    }

    fun getKingPerimeter(index: Int): Array<Int> {
        val perimeter = mutableListOf<Int>()
        for (step in omniDirectional) {
            if (willFileOverflow(index, index + step)
                || !isWithinBoard(index + step)
            ) continue

            perimeter.add(index + step)
        }

        return perimeter.toTypedArray()
    }

    //returns bitboard that has index spaces occupied by allies marked as 1
    fun getAllyOccupiedSquares(indices: Array<Int>, isWPlayer: Boolean): ULong {
        val allyBoard = getColorBoard(isWPlayer)
        return flipBits(empty, indices) and allyBoard
    }

    fun isCheckMate(whiteTurn: Boolean): Boolean {
        val kingBoard = getPieceBoard(if (whiteTurn) 1 else 7)
        val kingIndex = getBoardIndices(kingBoard)

        if (kingIndex.isEmpty() || kingIndex.size != 1)
            throw IllegalStateException("There was either no or more than one King on the board!")

        val kingPerimeter = getKingPerimeter(kingIndex[0])
        val bitPerimeter = flipBits(empty, kingPerimeter)
        val threatenedSquares = getThreatenedSquares(kingPerimeter, whiteTurn)
        val occupied = getAllyOccupiedSquares(kingPerimeter, whiteTurn)

        //finds all squares that are neither occupied nor threatened by enemy pieces
        val availableSquares = (threatenedSquares xor occupied) xor bitPerimeter

        return isCheck(whiteTurn) && availableSquares.countOneBits() == 0
    }

    fun recordMove(move: ChessMove){
        moveHistory.add(move)
    }

    fun getPrevMove(): Pair<Boolean, ChessMove> {
        if (moveHistory.isEmpty()) {
            return Pair(false, ChessMove())
        }

        return Pair(
            true,
            moveHistory[moveHistory.lastIndex]
        )
    }
}