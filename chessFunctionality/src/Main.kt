import chessBoard.ChessBoardManager


fun main() {
    val manager = ChessBoardManager()
    manager.initChessBoard()

    for (i in 0..63) {
        val file: Int = i%8
        val rank: Int = i/8
        if(manager.boardConfig[file][rank] != null){
            val chessPiece = manager.boardConfig[file][rank]?.read()
            val pieceMoves = manager.boardConfig[file][rank]?.readAvailableMoves()
            println("$chessPiece")
            println("$pieceMoves")
        }
    }
}