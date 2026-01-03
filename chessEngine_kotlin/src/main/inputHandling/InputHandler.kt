package inputHandling

import utils.isInAlgebraicNotation
import chessData.ChessMove
import chessData.EPieceType
import utils.isWhite
import java.util.Locale.getDefault
import kotlin.system.exitProcess

class InputHandler(
    private val keywords: Array<String> = arrayOf("help", "check", "an", "quit", "move", "demos", "demo", "reset"),
    private val automatedGame: Array<Array<String>> = arrayOf(
        arrayOf("f2f3", "e7e6", "g2g4", "d8h4"),
        arrayOf("e2e3", "f7f6", "g1h3", "g7g5", "d1h5"),
        arrayOf("e2e3", "b8a6", "a2a3", "d7d6", "b2b3", "c8e6", "c2c3", "d8d7", "d2d3", "e8c8"),
        arrayOf("g2g3", "a7a6", "f1h3", "b7b6", "g1f3", "c7c6", "e1g1"),
        arrayOf("d2d4", "a7a6", "d4d5", "c7c5", "d5c5"),
        arrayOf("f2f4", "g8h6", "f4f5", "g7g5", "f5g5", "b8c6", "g5g6", "a7a6", "g6g7", "d7d5", "g7g8")
    )
) {
    //region AutomatedGame
    private var automated = false
    private var gameStep = 0
    private var gameIndex: Int? = 0
    //endregion

    fun initialize() {
        automated = false
        gameStep = 0
        gameIndex = 0
    }

    /*reads player input (if no automated game is running)*/
    fun readInput(): Pair<EOutputType, ChessMove?> {
        if (automated) {
            if (gameStep < automatedGame[gameIndex!!].lastIndex) {
                gameStep++
                return Pair(EOutputType.Move, handleMoveCmd(automatedGame[gameIndex!!][gameStep]))
            } else {
                automated = false
                gameStep = 0
                gameIndex = 0
                println("Run of Demo complete. Please reset the game by typing 'reset'.")
            }
        }

        println("Please enter a command (type 'help' for a list of commands):")
        print("> ")
        val input = readln()
        return handleInput(input)
    }

    /*returns the type of input received and its chess move if there was one*/
    private fun handleInput(input: String): Pair<EOutputType, ChessMove?> {
        val normedInput = input.lowercase(getDefault())

        val keyword = extractKeyword(normedInput)
        println("The following command will be executed: " + if (keyword == "") "move chess piece" else keyword)

        //debug
        val output: Pair<EOutputType, ChessMove?> = when (keyword) {
            keywords[0] -> Pair(handleHelpCmd(), null)
            keywords[1] -> Pair(EOutputType.MoveCheck, handleCheckCmd(normedInput))
            keywords[2] -> Pair(handleAlgebraicNotationCmd(), null)
            keywords[3] -> exitProcess(0)
            keywords[5] -> Pair(handleDemosCmd(), null)
            keywords[6] -> Pair(EOutputType.Move, handleDemoCmd(normedInput))
            keywords[7] -> Pair(EOutputType.Reset, null)
            else -> Pair(EOutputType.Move, handleMoveCmd(normedInput))
        }
        return output
    }

    private fun handleHelpCmd(): EOutputType {
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

    /*return chess move for inquiry about possible moveset for the given square index*/
    private fun handleCheckCmd(input: String): ChessMove? {
        val cleanedString = cleanString(keywords[1], input)

        if (cleanedString.length == 2
            && isInAlgebraicNotation(cleanedString)
        ) {
            return ChessMove(cleanedString)
        }
        return null
    }

    private fun handleAlgebraicNotationCmd(): EOutputType {
        println(
            "Algebraic notation is the standard method of chess notation to record and describe moves. It consists of two characters.\n" +
                    "The first character (a -> h) describes the columns of a chessboard from left to right.\n" +
                    "The second character (1 -> 8) describes the rows of the chessboard starting from the bottom.\n" +
                    "Example: The bottom left square would be notated as a1. The top right square as h8.\n"
        )
        return EOutputType.None
    }

    private fun handleDemosCmd(): EOutputType {
        println(
            "The following (semi-nonsense) chess demos are preprogrammed and can be run through via the 'auto' command.\n" +
                    "> 0: Fool's Mate. Checkmate in four moves. Black wins.\n" +
                    "> 1: Reverse Fool's Mate. Checkmate in five moves. White wins.\n" +
                    "> 2: Black King Long Rochade.\n" +
                    "> 3: White King Short Rochade.\n" +
                    "> 4: Pawn en Passant.\n" +
                    "> 5: Pawn transformation."
        )
        return EOutputType.None
    }

    /*toggles game automation and returns the first move of the chosen automated game*/
    private fun handleDemoCmd(input: String): ChessMove? {
        val cleanedString = cleanString(keywords[6], input)
        gameIndex = cleanedString.toIntOrNull()
        if (gameIndex == null || gameIndex!! > automatedGame.lastIndex) {
            println("GameIndex was not valid or does not exist. Automated game cannot be started.")
            return null
        }
        automated = true
        return handleMoveCmd(automatedGame[gameIndex!!][gameStep])
    }

    /*extracts and returns chess move from input*/
    private fun handleMoveCmd(input: String): ChessMove? {
        val moveCoord = extractMove(input)

        if (moveCoord.first) {
            return moveCoord.second!!
        }

        println("Move was not in algebraic notation. Please enter 'an' for more info.")
        return null
    }

    /*cleans string and returns the found chess move (if it was valid)*/
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

    /*extracts the first keyword (such as 'help' or 'moves') from the given string*/
    fun extractKeyword(string: String): String {
        for (keyword in keywords) {
            if (string.contains(keyword)) {
                return keyword
            }
        }
        return ""
    }

    /*cleans string of keywords and spaces*/
    fun cleanString(keyword: String = "", string: String): String {
        var cleanedString = string
        cleanedString = cleanedString.replace(keyword, "")
        cleanedString = cleanedString.replace("to", "")
        cleanedString = cleanedString.replace(" ", "")
        return cleanedString
    }

    fun isMoveInputValid(input: String): Boolean {
        return input.length == 4
    }

    /*reads player input when pawn transformation is achieved*/
    fun inquirePawnTransform(piece: EPieceType): Int{
        println("$piece has reached board edge!")
        var chosen: Int? = null
        while(chosen == null) {
            print("Please choose which chess piece it should promote to by typing its corresponding number:\n" +
                    "> 0: Bishop\n" +
                    "> 1: Knight\n" +
                    "> 2: Queen\n" +
                    "> 3: Rook\n--------------\n" +
                    "> " )
            var input = readln()
            input = input.lowercase()
            input = cleanString(string = input)
            chosen = input.toIntOrNull()

            if (chosen == null || chosen !in 0..3) {
                chosen = null
                print("Input was not valid. ")
            } else {
                val isWPlayer = isWhite(piece)
                println("\nPromoting pawn to chosen piece!")
                return when(chosen){
                    0 -> if(isWPlayer) 0 else 6
                    1 -> if(isWPlayer) 2 else 8
                    2 -> if(isWPlayer) 4 else 10
                    else -> if(isWPlayer) 5 else 11
                }
            }
        }
        return -1
    }

    fun inquireReplay(): EOutputType {
        println("Enter 'reset' to start again and 'quit' to quit the program:")
        print("> ")
        val input = readln()
        when (input) {
            "reset" -> return EOutputType.Reset
            "quit" -> exitProcess(0)
            else -> {
                println("Input was not valid.")
                inquireReplay()
            }
        }
        return EOutputType.None
    }
}