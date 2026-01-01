import junit.framework.TestCase.assertTrue
import mock.MockBoardStateManager
import org.junit.Test
import utils.empty
import utils.getBoardIndices

class BoardStateManagerTest {
    private val boardStateManager = MockBoardStateManager()

    @Test
    fun `areSquaresThreatened returns true when all given indices are threatened by enemy chess pieces`() {
        boardStateManager.initialize(
            arrayOf(
                empty, empty, empty, 0x20020008200400u, empty, empty,
                empty, empty, 0x800000000000000u, empty, 0x8u, 0x100000000000u
            )
        )

        val result = boardStateManager.areSquaresThreatened(getBoardIndices(0x20020008200400u), true)
        assertTrue(result)
    }


    @Test
    fun `isCheck returns true when King of current color is in check`() {
        boardStateManager.initialize(
            arrayOf(
                empty, 0x80000u, empty, empty, empty, empty,
                0x400000000000u, empty, empty, empty, empty, empty
            )
        )

        val result = boardStateManager.isCheck(true)
        assertTrue(result)
    }

    @Test
    fun `isCheckmate returns true when King of current color is in checkmate`() {
        boardStateManager.initialize(
            arrayOf(
                empty, 0x20u, empty, empty, empty, empty,
                0x2000000u, empty, empty, empty, 0x4000u, 0x1080u
            )
        )

        val result = boardStateManager.isCheckmate(true)
        assertTrue(result)
    }
}