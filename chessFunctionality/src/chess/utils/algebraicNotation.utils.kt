package chess.utils

import java.lang.Character.toChars

fun toAlgebraic(index: Int): String {
    val file = index % 8 + 97
    val rank = index / 8 + 49

    if(file !in 97..104 || rank !in 49..56){
        throw IllegalArgumentException("Index is not within range cannot be converted to Algebraic Notation")
    }

    val fileAlgebraic = toChars(file)
    val rankAlgebraic = toChars(rank)

    return (fileAlgebraic + rankAlgebraic).concatToString()
}

fun toIndex(algebraic: String): Int{
    if(algebraic.length != 2){
        throw IllegalArgumentException("Length of string parameter must be 2")
    }
    val file = algebraic[0].code - 97
    val rank = algebraic[1].code - 49
    return rank * 8 + file
}

fun isInAlgebraicNotation(input: String): Boolean {
    for(i in input.indices) {
        if(i%2 == 0){
            if(input[i] !in 'a'..'h'){
                return false
            }
        } else {
            if(input[i] !in '1'..'8'){
                return false
            }
        }
    }
    return true
}