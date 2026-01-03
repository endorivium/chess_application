import chessData.EPieceType
import chessPieceImplementation.baseImplementation.SingleStep
import junit.framework.TestCase.assertEquals
import org.junit.Test
import utils.omniDirectional


class SingleStepTest {
    private val singleStep = SingleStep(EPieceType.BKing, omniDirectional)

    @Test
    fun `findMoves returns all possible moves that a single step chess piece can execute from B3 on a default board`(){
        val board = 0xffff00000000ffffu

        val result = singleStep.findMoves(17, board)
        val expected = 0xa0e000000000u

        assertEquals(expected, result)
    }

    @Test
    fun `findAttacks returns all possible attacks on enemy pieces that a single step chess piece can execute from B3 on a default board`(){
        val allyBoard: ULong = 0xffffu
        val enemyBoard = 0xffff000000000000u

        val result = singleStep.findAttacks(17, allyBoard, enemyBoard)
        val expected = 0xe0000000000000u
        assertEquals(expected, result)
    }

    @Test
    fun `findAllPossibleAttacks returns all possible attacks that a single step chess piece can execute from B3 on a default board`(){
        val allyBoard: ULong = 0xffffu
        val enemyBoard = 0xffff000000000000u

        val result = singleStep.findAllPossibleAttacks(17, allyBoard xor enemyBoard, allyBoard, enemyBoard)
        val expected = 0xe0a0e000000000u
        assertEquals(expected, result)
    }
}