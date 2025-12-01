package chess_board

import chess_piece.*

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
    wRook, wKnight, wBishop, wKing, wQueen, wBishop, wKnight, wRook)

    class ChessBoardManager {
    var board = Array(8){Array<ChessPiece?>(8){null}}

    //TODO: init via save file
    fun initChessBoard(boardConfig: Array<Pair<PType, PColor>> = defaultBoardConfig){
        for(i in boardConfig.indices) {
            if(boardConfig[i].first == PType.None){
                board[i/8][i%8] = null
            } else {
                board[i/8][i%8] = ChessPiece(
                    boardConfig[i].first,
                    boardConfig[i].second,
                    BoardCoords(i/8, i%8))
            }
        }
    }
}