package chessBoard

import chessPiece.*

//region defaultChessPiecesInfo
val bRook = Pair(PType.Rook, PColor.Black)
val bKnight = Pair(PType.Knight, PColor.Black)
val bBishop = Pair(PType.Bishop, PColor.Black)
val bQueen = Pair(PType.Queen, PColor.Black)
val bKing = Pair(PType.King, PColor.Black)
val bPawn = Pair(PType.Pawn, PColor.Black)
val wRook = Pair(PType.Rook, PColor.White)
val wKnight = Pair(PType.Knight, PColor.White)
val wBishop = Pair(PType.Bishop, PColor.White)
val wQueen = Pair(PType.Queen, PColor.White)
val wKing = Pair(PType.King, PColor.White)
val wPawn = Pair(PType.Pawn, PColor.White)
val None = Pair(PType.None, PColor.None)
//endregion

val defaultBoardConfig = arrayOf(
    bRook, bKnight, bBishop, bKing, bQueen, bBishop, bKnight, bRook,
    bPawn, bPawn, bPawn, bPawn, bPawn, bPawn, bPawn, bPawn,
    None, None, None, None, None, None, None, None,
    None, None, None, None, None, None, None, None,
    None, None, None, None, None, None, None, None,
    None, None, None, None, None, None, None, None,
    wPawn, wPawn, wPawn, wPawn, wPawn, wPawn, wPawn, wPawn,
    wRook, wKnight, wBishop, wKing, wQueen, wBishop, wKnight, wRook
)

data class SpaceOccupation(val isOccupied: Boolean, val isEnemy: Boolean);

class ChessBoardManager {
    var boardConfig = Array(8) { Array<ChessPiece?>(8) { null } }
        private set

    var selectedPiece: ChessPiece? = null
        private set

    //TODO: init via save file
    fun initChessBoard(boardConfig: Array<Pair<PType, PColor>> = defaultBoardConfig) {
        for (i in boardConfig.indices) {
            val file = i%8
            val rank = i/8
            if (boardConfig[i].first == PType.None) {
                this@ChessBoardManager.boardConfig[file][rank] = null
            } else {
                when(boardConfig[i].first) {
                    PType.Pawn -> this@ChessBoardManager.boardConfig[file][rank] = Pawn(
                        boardConfig[i].first,
                        boardConfig[i].second,
                        SquareCoords(file, rank),
                        this)
                    else -> this@ChessBoardManager.boardConfig[file][rank] = ChessPiece(
                        boardConfig[i].first,
                        boardConfig[i].second,
                        SquareCoords(file, rank),
                        this)
                }
            }
        }

        for (i in boardConfig.indices) {
            val file = i%8
            val rank = i/8
            this@ChessBoardManager.boardConfig[file][rank]?.findAvailableMoves()
        }
    }

    fun printChessBoard() {
        for (i in 0..63) {
            val file: Int = i % 8
            val rank: Int = i / 8
            if (boardConfig[file][rank] != null) {
                val chessPiece = boardConfig[file][rank]?.read()
                val pieceMoves = boardConfig[file][rank]?.readAvailableMoves()
                println("$chessPiece $pieceMoves")
            }
        }
    }

    fun isMoveWithinBounds(coords: SquareCoords): Boolean{
        return coords.file in 0..<8 && coords.rank in 0..<8
    }

    // TODO: check path to square if isGrounded
    fun isSquareOccupied(coords: SquareCoords, pieceColor: PColor, isGrounded: Boolean = true): SpaceOccupation {
        if(!isMoveWithinBounds(coords))
            throw IndexOutOfBoundsException("$coords is out of bounds")

        if(boardConfig[coords.file][coords.rank] == null)
            return SpaceOccupation(false, false)

        return SpaceOccupation(true, pieceColor == boardConfig[coords.file][coords.rank]!!.color)
    }

    fun isCheckMate(): Boolean{
        return true;
    }
}