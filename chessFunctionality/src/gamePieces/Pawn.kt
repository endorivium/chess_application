package gamePieces

import bitoperation.utils.empty
import bitoperation.utils.getBit

import bitoperation.utils.setBit
import gameState.ChessMove

class Pawn(
    val piece: EPieceType,
    var mod: Int = 1
): ChessPiece() {
    init{
        if(piece == EPieceType.BPawn || piece == EPieceType.WPawn) {
            mod = if(piece.compareTo(EPieceType.BPawn) == 0) -1 else 1
        } else {
            throw IllegalArgumentException("Pawn must be assigned Pawn enum value (WPawn, BPawn).")
        }
    }

    override fun getPossibleMoves(posIndex: Int): ULong {
        val push = getPush(posIndex)
        var attack = getAttack(posIndex)
        attack = attack and boardStateManager.getEnemyPieceBoard(piece)

        //gets pushes that are actually possible by combining (xor) and then excluding (and)
        var possibleMoves: ULong = (push xor boardStateManager.getBoardState()) and push
        possibleMoves = possibleMoves xor attack
        return possibleMoves
    }

    fun getPush(posIndex: Int): ULong {
        val singlePush = pushSingle(posIndex, EPieceType.WPawn)
        val doublePush = pushDouble(posIndex, EPieceType.WPawn)
        val boardState = boardStateManager.getBoardState()
        var forwardMoves = singlePush xor doublePush
        forwardMoves = (forwardMoves xor boardState) and forwardMoves

        return forwardMoves
    }

    fun getAttack(posIndex: Int): ULong {
        var leftAttack: ULong = empty
        if(posIndex%8 != 0 && posIndex in 8..55){
            leftAttack = setBit(bitIndex = posIndex + mod*7)
        }

        var rightAttack: ULong = empty
        if(posIndex%7 != 0 && posIndex in 8..55){
            rightAttack = setBit(bitIndex = posIndex + mod*9)
        }

        val attack: ULong = leftAttack xor rightAttack xor enPassantMove(posIndex)
        return attack
    }

    //TODO: enPassant should only work if pawn piece was moves in previous move by enemy
    fun enPassantMove(posIndex: Int): ULong {
        val boardState = boardStateManager.getBoardState()
        val enPassantIndexLeft = if(posIndex%8 != 0) posIndex - 1 else -1
        val enPassantIndexRight = if(posIndex%7 != 0) posIndex + 1 else -1

        var enPassant: ULong = 0x0u
        if(enPassantIndexLeft != -1 && getBit(boardState, enPassantIndexLeft)) {
            enPassant = setBit(bitIndex = enPassantIndexLeft)
        }
        if(enPassantIndexRight != -1 && getBit(boardState, enPassantIndexRight)) {
            enPassant = enPassant xor setBit(bitIndex = enPassantIndexRight)
        }
        return enPassant
    }

    fun pushSingle(posIndex: Int, piece: EPieceType): ULong{
        var singlePush: ULong = empty
        if(isMoveWithinBoard(posIndex)){
            singlePush = singlePush xor setBit(singlePush, posIndex + mod*8)
        }
        //printBitDebug(singlePush, "pushSingle: ")
        return singlePush
    }

    fun pushDouble(posIndex: Int, piece: EPieceType): ULong{
        var doublePush: ULong = empty
        if(posIndex in 8..15 || posIndex in 48..55){
            doublePush = setBit(doublePush, posIndex + mod*16)
        }
        //printBitDebug(doublePush, "pushDouble: ")
        return doublePush
    }

    override fun canExecuteMove(move: ChessMove): Boolean {
        return super.canExecuteMove(move)
    }

    fun isMoveWithinBoard(posIndex: Int): Boolean {
        return posIndex in 8..55
    }
}