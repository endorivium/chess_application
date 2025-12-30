package inputHandling

import chess.utils.isInAlgebraicNotation
import chessData.ChessMove
import java.util.Locale.getDefault
import kotlin.system.exitProcess

class InputHandler(
    val keywords: Array<String> = arrayOf("help", "check", "an", "quit", "move", "demo", "demos", "reset"),
    val automatedGame: Array<Array<String>> =
        arrayOf(
            arrayOf("f2f3", "e7e6", "g2g4", "d8h4"),
            arrayOf("e2e3", "b8a6", "a2a3", "d7d6", "b2b3", "c8e6", "c2c3", "d8d7", "d2d3", "e8c8"),
            arrayOf("g2g3", "a7a6", "f1h3", "b7b6", "g1f3", "c7c6", "e1g1")
        )
) {
    //region AutomatedGame
    var automated = false
    var gameStep = 0
    var gameIndex: Int? = 0
    //endregion

    fun initialize() {
        automated = false
        gameStep = 0
        gameIndex = 0
    }

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
        println(
            "Commands are not case sensitive. All move commands must be written in algebraic notation.\n" +
                    "> help: get a list of all available commands that allow interaction with the game\n" +
                    "> check <SquareCoordinates>: get all available moves that the chess piece on that square can take\n" +
                    "> an: gives more information on algebraic notation which is needed to move chess pieces\n" +
                    "> move: initiates the movement of a chess piece via algebraic notation, can be omitted\n" +
                    "> demo <index>: initiates an automated game of chess, type 'demos' to see what preprogrammed games are available\n" +
                    "> reset: resets the chess program\n" +
                    "> quit: terminates the program"
        )
        return EOutputType.None
    }

    fun handleDemosCmd(): EOutputType {
        println(
            "The following chess demos are preprogrammed and can be run through via the 'auto' command.\n" +
                    "> 0: Fool's Mate. Checkmate in four moves.\n" +
                    "> 1: Black King Long Rochade\n" +
                    "> 2: White King Short Rochade"
                    //"> 3: Chess Piece Demo. All pieces move and attack."
        )
        return EOutputType.None
    }

    fun handleCheckCmd(input: String): ChessMove? {
        val cleanedString = cleanString(keywords[1], input)

        if (cleanedString.length == 2
            && isInAlgebraicNotation(cleanedString)
        ) {
            return ChessMove(cleanedString)
        }
        return null
    }

    fun handleAlgebraicNotationCmd(): EOutputType {
        println(
            "Algebraic notation is the standard method of chess notation to record and describe moves. It consists of two characters.\n" +
                    "The first character (a -> h) describes the columns of a chessboard from left to right.\n" +
                    "The second character (1 -> 8) describes the rows of the chessboard starting from the bottom.\n" +
                    "Example: The bottom left square would be notated as a1. The top right square as h8.\n"
        )
        return EOutputType.None
    }

    fun handleMoveCmd(input: String): ChessMove? {
        val moveCoord = extractMove(input)

        if (moveCoord.first) {
            return moveCoord.second!!
        }

        println("Move was not in algebraic notation. Please enter 'an' for more info.")
        return null
    }

    fun handleDemoCmd(input: String): ChessMove? {
        val cleanedString = cleanString(keywords[5], input)
        gameIndex = cleanedString.toIntOrNull()
        if (gameIndex == null) {
            println("GameIndex was not valid or does not exist. Automated game cannot be started.")
            return null
        }
        automated = true
        return handleMoveCmd(automatedGame[gameIndex!!][gameStep])
    }

    fun extractMove(input: String): Pair<Boolean, ChessMove?> {
        val cleanedString = cleanString(keywords[4], input)

        if (isMoveInputValid(cleanedString) && isInAlgebraicNotation(cleanedString)) {
            return Pair(
                true, ChessMove(
                    cleanedString.take(2),
                    cleanedString.substring(2)
                )
            )
        }
        return Pair(false, null)
    }

    fun cleanString(keyword: String, string: String): String {
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
        println("The following command will be executed: " + if (keyword == "") "move chess piece" else keyword)

        //debug
        val output: Pair<EOutputType, ChessMove?> = when (keyword) {
            keywords[0] -> Pair(handleHelpCmd(), null)
            keywords[1] -> Pair(EOutputType.MoveCheck, handleCheckCmd(normedInput))
            keywords[2] -> Pair(handleAlgebraicNotationCmd(), null)
            keywords[3] -> exitProcess(0)
            keywords[5] -> Pair(EOutputType.Move, handleDemoCmd(normedInput))
            keywords[6] -> Pair(handleDemosCmd(), null)
            keywords[7] -> Pair(EOutputType.Reset, null)
            else -> Pair(EOutputType.Move, handleMoveCmd(normedInput))
        }
        return output
    }

    fun playAgain(): EOutputType {
        println("Enter 'reset' to start again and 'quit' to quit the program:")
        print("> ")
        val input = readln()
        when (input) {
            "reset" -> {
                return EOutputType.Reset
            }
            "quit" -> {
                exitProcess(0)
            }
            else -> {
                println("Input was not valid.")
                playAgain()
            }
        }
        return EOutputType.None
    }

    fun readInput(): Pair<EOutputType, ChessMove?> {
        if (automated) {
            if (gameStep < automatedGame[gameIndex!!].lastIndex) {
                gameStep++
                return Pair(EOutputType.Move, handleMoveCmd(automatedGame[gameIndex!!][gameStep]))
            } else {
                automated = false
                gameStep = 0
                gameIndex = 0
                println("Run of Demo complete.")
            }
        }

        println("Please enter a command (type 'help' for a list of commands):")
        print("> ")
        val input = readln()
        return handleInput(input)
    }
}