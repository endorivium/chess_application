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
import chessPieceImplementation.RuleBook


open class BoardStateManager(
    val moveHistory: MutableList<ChessMove> = mutableListOf()
) {
    protected var ruleBook: RuleBook = RuleBook(this)

    //region GameStateVariables
    var wKingMoved = false
    var wLeftRookMoved = false
    var wRightRookMoved = false

    var bKingMoved = false
    var bLeftRookMoved = false
    var bRightRookMoved = false

    var pawnTransform = false
    //endregion

    //region BlackBoard
    private val wBishopBoard: ULong = 0x2400000000000000u
    private val wKingBoard: ULong = 0x800000000000000u
    private val wKnightBoard: ULong = 0x4200000000000000u
    private val wPawnBoard: ULong = 0xFF000000000000u
    private val wQueenBoard: ULong = 0x1000000000000000u
    private val wRookBoard: ULong = 0x8100000000000000u
    //endregion

    //region WhiteBoard
    private val bBishopBoard: ULong = 0x24u
    private val bKingBoard: ULong = 0x8u
    private val bKnightBoard: ULong = 0x42u
    private val bPawnBoard: ULong = 0xFF00u
    private val bQueenBoard: ULong = 0x10u
    private val bRookBoard: ULong = 0x81u
    //endregion

    protected var boards = arrayOf(
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

    open fun initialize() {
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

    fun execChessMove(move: ChessMove, whiteTurn: Boolean): Pair<Boolean, ChessMove?> {
        move.chessPiece = getPieceAt(move.initialIndex, whiteTurn)
            ?: return Pair(false, null)

        val pieceRule = ruleBook.getRules(move.chessPiece)

        val moveExec = pieceRule.canExecuteMove(
            move, getBoardState(),
            getColorBoard(whiteTurn), getColorBoard(!whiteTurn)
        )
        if (moveExec.first) {
            when (moveExec.second!!) {
                EMoveType.Move -> execMove(move.chessPiece, move)
                EMoveType.Attack -> execAttack(move.chessPiece, move, whiteTurn)
                else -> execRochade(move.chessPiece, move)
            }

            checkKingRooksMoved(move.chessPiece, move.initialIndex)
            recordMove(move)
            return Pair(true, if (pawnTransform) move else null)
        }
        return Pair(false, null)
    }

    /*returns the chess piece at the given index but only if it's the corresponding color*/
    fun getPieceAt(index: Int, isWhite: Boolean): EPieceType? {
        val pieceBit = flipBit(empty, index)

        for (i in boards.indices) {
            if ((pieceBit and boards[i]).countOneBits() >= 1) {
                if (isWhite(EPieceType.fromInt(i)!!) == isWhite)
                    return EPieceType.fromInt(i)
            }
        }
        return null
    }

    /*returns the current state of the board*/
    fun getBoardState(): ULong {
        var board: ULong = empty

        for (piece in boards) {
            board = board xor piece
        }
        return board
    }

    /*returns current board of the given color*/
    fun getColorBoard(isWhite: Boolean): ULong {
        var board: ULong = empty
        if (isWhite) {
            for (i in 0..5) {
                board = board xor getPieceBoard(EPieceType.fromInt(i)!!)
            }
        } else {
            for (i in 6..11) {
                board = board xor getPieceBoard(EPieceType.fromInt(i)!!)
            }
        }
        return board
    }

    /*returns the bitboard of the given chess piece*/
    fun getPieceBoard(piece: EPieceType): ULong {
        if (piece.ordinal !in boards.indices)
            throw IllegalStateException("$piece was not found in Board Set!")

        return boards[piece.ordinal]
    }

    /*return array of all chess piece bitboards*/
    fun getPieceBoards(): Array<ULong> {
        val pieceBoards = Array(12) { empty }

        for (i in pieceBoards.indices) {
            pieceBoards[i] = boards[i]
        }

        return pieceBoards
    }

    private fun execMove(piece: EPieceType, move: ChessMove) {
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

    /*checks if any kings or rooks have moved and flips boolean if true*/
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

    private fun recordMove(move: ChessMove) {
        moveHistory.add(move)
    }

    /*called from pawn chess piece to notify future execution of pawn transformation at end of move execution*/
    fun notifyPawnTransformation() {
        pawnTransform = true
    }

    /*deletes pawn and puts chosen chess piece on the corresponding board*/
    fun execPawnTransformation(move: ChessMove, transform: EPieceType) {
        boards[move.chessPiece.ordinal] = flipBit(boards[move.chessPiece.ordinal], move.targetIndex)
        boards[transform.ordinal] = flipBit(boards[transform.ordinal], move.targetIndex)
        pawnTransform = false
    }

    /*checks if the king of the current player is in checkmate*/
    fun isCheckmate(whiteTurn: Boolean): Boolean {
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

    /*returns indices of squares surrounding the king at the given index*/
    private fun getKingPerimeter(index: Int): Array<Int> {
        val perimeter = mutableListOf<Int>()
        for (step in omniDirectional) {
            if (willFileOverflow(index, index + step)
                || !isWithinBoard(index + step)
            ) continue

            perimeter.add(index + step)
        }

        return perimeter.toTypedArray()
    }

    /*returns bitboard with all squares that are occupied by allies*/
    private fun getAllyOccupiedSquares(indices: Array<Int>, isWPlayer: Boolean): ULong {
        val allyBoard = getColorBoard(isWPlayer)
        return flipBits(empty, indices) and allyBoard
    }

    /*checks if the king of the current player is in check*/
    fun isCheck(whiteTurn: Boolean): Boolean {
        val kingBoard = getPieceBoard(if (whiteTurn) 1 else 7)
        val kingIndex = getBoardIndices(kingBoard)

        if (kingIndex.isEmpty() || kingIndex.size != 1)
            throw IllegalStateException("There was either no or more than one King on the board!")

        val threatened = isSquareThreatened(kingIndex[0], whiteTurn)
        return threatened
    }

    /*returns bitboard of all enemy threatened squares within the given indices array*/
    private fun getThreatenedSquares(indices: Array<Int>, isWPlayer: Boolean): ULong {
        var threatened: ULong = empty
        for (index in indices) {
            if (isSquareThreatened(index, isWPlayer)) {
                threatened = threatened xor flipBit(empty, index)
            }
        }
        return threatened
    }

    /*returns true if all squares are threatened by enemy pieces*/
    fun areSquaresThreatened(indices: Array<Int>, isWPlayer: Boolean): Boolean {
        for (index in indices) {
            if (isSquareThreatened(index, isWPlayer)) return true
        }
        return false
    }

    /*returns if the given square is threatened by any enemy pieces (done through simulation)*/
    private fun isSquareThreatened(index: Int, isWPlayer: Boolean): Boolean {
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

    /*simulate an attack with the given indices*/
    private fun simulateAtk(attacker: Int, target: Int): Boolean {
        val simulated = ChessMove(initialIndex = attacker, targetIndex = target)
        val chessPiece: EPieceType = getPieceAt(attacker)
            ?: throw IllegalArgumentException("Attacker should not be null!")

        val pieceRule = ruleBook.getRules(chessPiece)

        val white = isWhite(chessPiece)
        return pieceRule.canExecuteMove(
            simulated, getBoardState(),
            empty, getColorBoard(!white), true
        ).first
    }

    /*returns chess piece at the given index, color is irrelevant*/
    fun getPieceAt(index: Int): EPieceType? {
        val pieceBit = flipBit(empty, index)

        for (i in boards.indices) {
            if ((pieceBit and boards[i]).countOneBits() >= 1) {
                return EPieceType.fromInt(i)
            }
        }
        return null
    }

    /*overload of above, return bitboard via index*/
    fun getPieceBoard(index: Int): ULong {
        if (index !in boards.indices)
            throw IllegalArgumentException("$index was out of bounds for boards!")

        return boards[index]
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

    /*find possible squares that the chess piece at the given index (if there is one) can move to*/
    fun findPossibleMoves(move: ChessMove, whiteTurn: Boolean): Pair<EPieceType?, Array<Int>> {
        val chessPiece: EPieceType = getPieceAt(move.initialIndex, whiteTurn)
            ?: return Pair(null, emptyArray())

        val pieceRule = ruleBook.getRules(chessPiece)

        val possibleMoves = pieceRule.getPieceMoveSet(
            move.initialIndex, getBoardState(),
            getColorBoard(whiteTurn), getColorBoard(!whiteTurn)
        )

        return Pair(chessPiece, getBoardIndices(possibleMoves.move))
    }

    /*returns true if all squares in the given array are occupied by other pieces, irrelevant of their color*/
    fun areSquaresOccupied(indices: Array<Int>): Boolean {
        for (index in indices) {
            val square = flipBit(empty, index)
            val occupied = square and getBoardState()
            if (occupied.countOneBits() != 0) return true
        }
        return false
    }

    /*returns if King, left Rook, right Rook have moved dependent on asking color*/
    fun haveKingRooksMoved(isWPlayer: Boolean): Triple<Boolean, Boolean, Boolean> {
        if (isWPlayer)
            return Triple(wKingMoved, wLeftRookMoved, wRightRookMoved)
        return Triple(bKingMoved, bLeftRookMoved, bRightRookMoved)
    }
}