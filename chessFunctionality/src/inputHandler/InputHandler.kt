package inputHandler

import gameState.ChessMove
import java.util.Locale.getDefault
import kotlin.system.exitProcess

class InputHandler(
    val commandKeywords: Array<String> = arrayOf("help", "check", "an", "quit")) {
    //TODO: check input for errors and invalidity
    //TODO: convert input to coordinates
    //TODO: add keywords like help (to get all possible commands), moves [field] (to get all possible moves of piece)

    //extracts the first keyword (such as 'help' or 'moves' in the given string
    fun extractKeyword(string: String): String {
        for (keyword in commandKeywords) {
            if (string.contains(keyword)) {
                return keyword
            }
        }
        return ""
    }

    //handles the help cmd by printing out a list of all possible game commands
    fun handleHelpCmd(){
        println("Commands are not case sensitive. All move commands must be written in algebraic notation.\n" +
                "> Help: get a list of all available commands that allow interaction with the game\n" +
                "> Check <SquareCoordinate>: get all available moves that the chess piece on that square can take\n" +
                "> <SquareCoordinate> to <SquareCoordinate>: allows the current player to move a chess piece from one square to another")

        readInput()
    }

    fun handleMoveCheckCmd(command: String){
        //TODO: get possible moves from chess piece on square
    }

    fun handleAlgebraicNotationCmd(){
        println("Algebraic notation is the standard method of chess notation to record and describe moves. It consists of two characters.\n" +
                "The first character (a -> h) describes the columns of a chessboard from left to right.\n" +
                "The second character (1 -> 8) describes the rows of the chessboard starting from the bottom.\n" +
                "Example: The bottom left square would be notated as a1. The top right square as h8.\n")

        readInput()
    }

    fun handleMoveCmd(command: String): ChessMove?{
        val moveCoord = extractMove(command)

        if(moveCoord.first) {
            return moveCoord.second!!
        }

        println("Move was not in algebraic notation. Please enter 'an' for more info on the subject.")
        readInput()
        return null
    }

    fun extractMove(input: String): Pair<Boolean, ChessMove?> {
        println("Initial Input: $input") //debug
        val cleanedString = cleanString(input)

        if(isMoveInputValid(cleanedString) && isInAlgebraicNotation(cleanedString)) {
            return Pair(true, ChessMove(
                cleanedString.take(2),
                cleanedString.substring(2)))
        }
        return Pair(false, null)
    }

    fun cleanString(string: String): String{
        var cleanedString = string
        cleanedString = cleanedString.replace("move", "")
        cleanedString = cleanedString.replace("to", "")
        cleanedString = cleanedString.replace(" ", "")
        return cleanedString
    }

    fun isMoveInputValid(input: String): Boolean {
        return input.length == 4
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

    fun handleInput(input: String): ChessMove? {
        val normedInput = input.lowercase(getDefault())

        val keyword = extractKeyword(normedInput)
        println("The following command will be executed: " + if(keyword == "") "move chess piece" else keyword) //debug

        var playerMove: ChessMove? = null
        when(keyword){
            commandKeywords[0] -> handleHelpCmd()
            commandKeywords[1] -> handleMoveCheckCmd(normedInput)
            commandKeywords[2] -> handleAlgebraicNotationCmd()
            commandKeywords[3] -> exitProcess(0)
            else -> playerMove = handleMoveCmd(normedInput)
        }
        return playerMove
    }

    //valid moves formats: [field] to [field], move [field] to [field], [field] [field]
    fun readInput(): ChessMove? {
        println("Please enter a command (type 'help' for a list of commands):")
        print("> ")
        val input = readln()
        return handleInput(input)
    }
}