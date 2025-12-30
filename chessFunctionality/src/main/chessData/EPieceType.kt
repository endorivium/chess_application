package main.chessData

enum class EPieceType {
    WBishop,
    WKing,
    WKnight,
    WPawn,
    WQueen,
    WRook,
    BBishop,
    BKing,
    BKnight,
    BPawn,
    BQueen,
    BRook;

    companion object{
        fun fromInt(value: Int) = entries.firstOrNull { it.ordinal == value }
    }
}