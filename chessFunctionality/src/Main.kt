import chess_board.ChessBoardManager


fun main() {
    val manager = ChessBoardManager()
    manager.initChessBoard()

    for (i in 0..63) {
        val file: Int = i/8
        val rank: Int = i%8
        if(manager.board[i/8][i%8] != null){
            val chessPiece = manager.board[i/8][i%8]?.read()
            println("$file|$rank $chessPiece")
        }
    }
}