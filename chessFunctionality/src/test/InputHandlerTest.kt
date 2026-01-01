import chessData.ChessMove
import inputHandling.InputHandler
import junit.framework.TestCase.assertEquals
import org.junit.Test

class InputHandlerTest {
    private val inputHandler = InputHandler()

    @Test
    fun `cleanString returns square coords in algebraic notation with keyword removed`() {
        val result = inputHandler.cleanString("move", "move g1 to f3")
        assertEquals(result, "g1f3")
    }

    @Test
    fun `extractMove returns positive extraction result when input is in valid move notation`(){
        val result = inputHandler.extractMove("move e2 to d4")

        val expected = ChessMove("e2", "d4", 12, 28)
        assertEquals(true, result.first)
        assertEquals(expected, result.second)
    }
}