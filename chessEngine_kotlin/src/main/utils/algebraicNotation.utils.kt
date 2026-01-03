package utils

import java.lang.Character.toChars

/*converts index to algebraic notation*/
fun toAlgebraic(index: Int): String {
    val file = index % 8 + 65
    val rank = index / 8 + 49

    if(file !in 65..72 || rank !in 49..56){
        throw IllegalArgumentException("Index is not within range cannot be converted to Algebraic Notation")
    }

    val fileAlgebraic = toChars(file)
    val rankAlgebraic = toChars(rank)

    return (fileAlgebraic + rankAlgebraic).concatToString()
}

/*converts algebraic notation to index*/
fun toIndex(algebraic: String): Int{
    val normed = algebraic.lowercase()
    if(normed.length != 2){
        throw IllegalArgumentException("Length of string parameter must be 2")
    }
    val file = normed[0].code - 97
    val rank = normed[1].code - 49
    return rank * 8 + file
}

/*checks if the given string is in algebraic notation*/
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