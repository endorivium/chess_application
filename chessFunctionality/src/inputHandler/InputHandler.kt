package inputHandler

import chess.utils.isInAlgebraicNotation
import chess.utils.toAlgebraic
import chessData.ChessMove
import chessData.EPieceType
import java.util.Locale.getDefault
import kotlin.system.exitProcess

class InputHandler(
    val keywords: Array<String> = arrayOf("help", "check", "an", "quit", "move")) {
    //TODO: check input for errors and invalidity
    //TODO: convert input to coordinates
    //TODO: add keywords like help (to get all possible commands), moves [field] (to get all possible moves of piece)

    //extracts the first keyword (such as 'help' or 'moves' in the given string
    fun extractKeyword(string: String): String {
        for (keyword in keywords) {
            if (string.contains(keyword)) {
                return keyword
            }
        }
        return ""
    }

    //handles the help cmd by printing out a list of all possible game commands
    fun handleHelpCmd(): EOutputType {
        println("Commands are not case sensitive. All move commands must be written in algebraic notation.\n" +
                "> Help: get a list of all available commands that allow interaction with the game\n" +
                "> Check <SquareCoordinate>: get all available moves that the chess piece on that square can take\n" +
                "> <SquareCoordinate> to <SquareCoordinate>: allows the current player to move a chess piece from one square to another")
        return EOutputType.None
    }

    fun handleCheckCmd(input: String): ChessMove?{
        val cleanedString = cleanString(keywords[1], input)

        if(cleanedString.length == 2
            && isInAlgebraicNotation(cleanedString)) {
            return ChessMove(cleanedString)
        }
        return null
    }

    fun printCheck(chessPiece: EPieceType, index: Int, squares: MutableList<Int>){
        println(chessPiece.toString().substring(1) + " at " + toAlgebraic(index) + " can move to the following: ")
        for(i in squares.indices){
            if(i%3 == 0){
                print("\n")
            }
            print(toAlgebraic(squares[i]))
        }
        print("\n")
    }

    fun handleAlgebraicNotationCmd(): EOutputType {
        println("Algebraic notation is the standard method of chess notation to record and describe moves. It consists of two characters.\n" +
                "The first character (a -> h) describes the columns of a chessboard from left to right.\n" +
                "The second character (1 -> 8) describes the rows of the chessboard starting from the bottom.\n" +
                "Example: The bottom left square would be notated as a1. The top right square as h8.\n")
        return EOutputType.None
    }

    fun handleMoveCmd(input: String): ChessMove?{
        val moveCoord = extractMove(input)

        if(moveCoord.first) {
            return moveCoord.second!!
        }

        println("Move was not in algebraic notation. Please enter 'an' for more info.")
        return null
    }

    fun extractMove(input: String): Pair<Boolean, ChessMove?> {
        val cleanedString = cleanString(keywords[4], input)

        if(isMoveInputValid(cleanedString) && isInAlgebraicNotation(cleanedString)) {
            return Pair(true, ChessMove(
                cleanedString.take(2),
                cleanedString.substring(2)))
        }
        return Pair(false, null)
    }

    fun cleanString(keyword: String, string: String): String{
        var cleanedString = string
        cleanedString = cleanedString.replace(keyword, "")
        cleanedString = cleanedString.replace("to", "")
        cleanedString = cleanedString.replace(" ", "")
        return cleanedString
    }

    fun isMoveInputValid(input: String): Boolean {
        return input.length == 4
    }

    fun handleInput(input: String): Pair<EOutputType, ChessMove?> {
        val normedInput = input.lowercase(getDefault())

        val keyword = extractKeyword(normedInput)
        println("The following command will be executed: " + if(keyword == "") "move chess piece" else keyword) //debug

        var output: Pair<EOutputType, ChessMove?>
        when(keyword){
            keywords[0] -> output = Pair(handleHelpCmd(), null)
            keywords[1] -> output = Pair(EOutputType.MoveCheck, handleCheckCmd(normedInput))
            keywords[2] -> output = Pair(handleAlgebraicNotationCmd(), null)
            keywords[3] -> exitProcess(0)
            else -> output = Pair(EOutputType.Move, handleMoveCmd(normedInput))
        }
        return output
    }

    //valid moves formats: [field] to [field], move [field] to [field], [field] [field], [field][field]
    fun readInput(): Pair<EOutputType, ChessMove?> {
        println("Please enter a command (type 'help' for a list of commands):")
        print("> ")
        val input = readln()
        return handleInput(input)
    }
}