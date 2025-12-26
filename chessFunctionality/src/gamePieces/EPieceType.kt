package gamePieces

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
    BRook
}

inline fun <reified T: Enum<T>> Int.toEnum(): T?{
    return enumValues<T>().firstOrNull { it.ordinal == this }
}