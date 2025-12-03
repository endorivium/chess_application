package chessBoard

/*
* file = column
* rank = row
*/
data class SquareCoords (var file: Int, var rank: Int){

    constructor(square: SquareCoords) : this(square.file, square.rank){}
    fun convertToString(): String{
        val alphabeticalAssignment = file.toChar()+97
        val correctedRank = rank + 1
        return "$alphabeticalAssignment$correctedRank"
    }
}