package gamePieces

import bitoperation.utils.empty
import bitoperation.utils.printBitDebug
import bitoperation.utils.setBit
import gameState.ChessMove
import gameState.GameManager

class Pawn(gm: GameManager, piece: EPieceType): ChessPiece(gm ,piece) {

    override fun getPossibleMoves(posIndex: Int): ULong {
        val push = getPush(posIndex)
        var attack = getAttack(posIndex)
        attack = attack and gm.bSManager.getEnemyPieceBoard(piece)

        //gets pushes that are actually possible by combining (xor) and then excluding (and)
        var possibleMoves: ULong = (push xor gm.bSManager.getBoardState()) and push
        possibleMoves = possibleMoves xor attack
        printBitDebug(possibleMoves, "possibleMoves: ")
        return possibleMoves
    }

    fun getPush(posIndex: Int): ULong {
        val singlePush = pushSingle(posIndex)
        val doublePush = pushDouble(posIndex)
        val boardState = gm.bSManager.getBoardState()
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
        printBitDebug(attack, "attack: ")
        return attack
    }

    fun leftEnPassant(posIndex: Int): ULong {
        val prevMove = gm.getPrevMove()
        val enPassantIndexLeft = if(posIndex%8 != 0) posIndex - 1 else -1

        if(prevMove.first || enPassantIndexLeft == -1) return empty

        if(prevMove.second.chessPiece == getEnemyPawn()
            && prevMove.second.targetIndex == enPassantIndexLeft) {
            return setBit(bitIndex = enPassantIndexLeft)
        }
        return empty
    }

    fun rightEnPassant(posIndex: Int): ULong {
        val prevMove = gm.getPrevMove()
        val enPassantIndexRight = if(posIndex%7 != 0) posIndex + 1 else -1

        if(prevMove.first || enPassantIndexRight == -1) return empty

        if(prevMove.second.chessPiece == getEnemyPawn()
            && prevMove.second.targetIndex == enPassantIndexRight) {
            return setBit(bitIndex = enPassantIndexRight)
        }
        return empty
    }

    //TODO: enPassant should only work if pawn piece was moves in previous move by enemy
    fun enPassantMove(posIndex: Int): ULong {
//        val boardState = boardStateManager.getBoardState()
//        val enPassantIndexLeft = if(posIndex%8 != 0) posIndex - 1 else -1
//        val enPassantIndexRight = if(posIndex%7 != 0) posIndex + 1 else -1
//
//        var enPassant: ULong = empty
//        if(enPassantIndexLeft != -1 && getBit(boardState, enPassantIndexLeft)) {
//            enPassant = setBit(bitIndex = enPassantIndexLeft)
//        }
//        if(enPassantIndexRight != -1 && getBit(boardState, enPassantIndexRight)) {
//            enPassant = enPassant xor setBit(bitIndex = enPassantIndexRight)
//        }
//        return enPassant

        return leftEnPassant(posIndex) xor rightEnPassant(posIndex)
    }

    fun getEnemyPawn(): EPieceType {
        return if(piece == EPieceType.BPawn) EPieceType.BPawn else EPieceType.WPawn
    }

    fun pushSingle(posIndex: Int): ULong{
        var singlePush: ULong = empty
        if(isMoveWithinBoard(posIndex)){
            singlePush = singlePush xor setBit(singlePush, posIndex + mod*8)
        }
        //printBitDebug(singlePush, "pushSingle: ")
        return singlePush
    }

    fun pushDouble(posIndex: Int): ULong{
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