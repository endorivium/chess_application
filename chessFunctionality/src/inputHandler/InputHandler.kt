package inputHandler

import gameState.MoveCoordinates
import java.util.Locale.getDefault
import kotlin.system.exitProcess

class InputHandler(
    val moveHistory: MutableList<MoveCoordinates> = mutableListOf(),
    val commandKeywords: Array<String> = arrayOf("help", "check", "quit")) {
    //TODO: read input from cmd line
    //TODO: check input for errors and invalidity
    //TODO: convert input to coordinates
    //TODO: add keywords like help (to get all possible commands), moves [field] (to get all possible moves of piece)

    //valid moves formats: [field] to [field], move [field] to [field], [field] [field]
    fun readNextMove(){
        println("Please enter a command:")
        print("> ")
        val input = readln()
        handleInput(input)
    }

    fun handleInput(input: String) {
        val normedInput = input.lowercase(getDefault())

        val keyword = extractKeyword(normedInput)
        println("The following command will be executed: " + if(keyword == "") "move chess piece" else keyword)
        when(keyword){
            commandKeywords[0] -> handleHelpCmd()
            commandKeywords[1] -> handleMoveCheckCmd(normedInput)
            commandKeywords[2] -> exitProcess(0)
            else -> handleMoveCmd(normedInput)
        }
    }

    fun extractMove(string: String): MoveCoordinates {
        println("Initial Input: $string")
        var cleanedString = string
        cleanedString = cleanedString.replace("move", "")
        cleanedString = cleanedString.replace("to", "")
        cleanedString = cleanedString.replace(" ", "")
        return MoveCoordinates(cleanedString.take(2), cleanedString.substring(2))
    }

    fun handleMoveCmd(command: String){
        val moveCoord = extractMove(command)
        moveHistory.add(moveCoord)

        for(move in moveHistory){
            println(move.startCoord + "|" + move.endCoord)
        }

        readNextMove() //TODO: have game state perpetuate this
    }

    fun handleMoveCheckCmd(command: String){
        //TODO: get possible moves from chess piece on square
    }

    //extracts the first keyword (such as 'help' or 'moves' in the given string
    fun extractKeyword(string: String): String{
        for(keyword in commandKeywords){
            if(string.contains(keyword)){
                return keyword
            }
        }
        return ""
    }

    //handles the help cmd by printing out a list of all possible game commands
    fun handleHelpCmd(){
        println("help: get a list of all available commands that allow interaction with the game\n" +
                "check <SquareCoordinate>: get all available moves that the chess piece on that square can take\n" +
                "<SquareCoordinate> to <SquareCoordinate>: allows the current player to move a chess piece from one square to another")

        readNextMove()
    }


}