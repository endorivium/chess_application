package inputParsing

import java.util.Locale
import java.util.Locale.getDefault
import java.util.Scanner

class InputParser(val inputHistory: MutableList<Array<String>> = mutableListOf()) {
    //TODO: read input from cmd line
    //TODO: check input for errors and invalidity
    //TODO: convert input to coordinates

    //valid moves formats: [field] to [field], move [field] to [field], [field] [field]
    fun readNextMove(){
        print("Input Move: ")
        val input = readln()
        val move = cleanString(input)

        for(m in move){
            println("Move piece: $m")
        }

        inputHistory.add(move)
    }

    fun cleanString(string: String): Array<String>{
        println("Initial Input: $string")
        var cleanedString = string
        cleanedString = cleanedString.lowercase(getDefault())
        cleanedString = cleanedString.replace("move", "")
        cleanedString = cleanedString.replace("to", "")
        cleanedString = cleanedString.replace(" ", "")
        return arrayOf(cleanedString.take(2), cleanedString.substring(2))
    }
}