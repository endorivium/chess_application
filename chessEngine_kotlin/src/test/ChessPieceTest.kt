import chessData.EPieceType
import chessPieceImplementation.baseImplementation.ChessPiece
import junit.framework.TestCase.assertEquals
import org.junit.Test
import utils.omniDirectional

class ChessPieceTest {
private val omniStep = ChessPiece(EPieceType.WQueen, omniDirectional)

    @Test
    fun `findMoves returns all possible moves that an infinite omnidirectional piece can execute (G4, default board)`(){
        val board = 0xffff00000000ffffu

        val result = omniStep.findMoves(30, board)
        val expected = 0x7fd070a0000u

        assertEquals(expected, result)
    }

    @Test
    fun `findAttacks returns all possible attacks on enemy pieces that an infinite omnidirectional piece (G4, default board)`(){
        val enemyBoard: ULong = 0xffffu
        val allyBoard = 0xffff000000000000u

        val result = omniStep.findAttacks(30, allyBoard, enemyBoard)
        val expected: ULong = 0x1200u
        assertEquals(expected, result)
    }

    @Test
    fun `findAllPossibleAttacks returns all possible attacks that an infinite omnidirectional piece can execute (G4, default board)`(){
        val enemyBoard: ULong = 0xffffu
        val allyBoard = 0xffff000000000000u

        val result = omniStep.findAllPossibleAttacks(17, allyBoard xor enemyBoard, allyBoard, enemyBoard)
        val expected = 0xe0bfe050484400u
        assertEquals(expected, result)
    }
}