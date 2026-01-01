import boardRendering.BoardRenderer
import org.junit.Test
import junit.framework.TestCase.assertEquals

class BoardRendererTest {
    private val renderer = BoardRenderer()

    @Test
    fun `refreshRendering returns string array (size = 8) configured per chess board rank`() {
        val mockBoard = 0xff01000000000000u
        val mockPieceBoards = arrayOf(
            0x2400000000000000u, 0x1000000000000000u, 0x4200000000000000u,
            0x1000000000000u, 0x800000000000000u, 0x8100000000000000u,
            0x0u, 0x0u, 0x0u, 0x0u, 0x0u, 0x0u
        )

        val result = renderer.refreshRendering(mockBoard, mockPieceBoards)

        val emptyRank = "[\uFF1D][\uFF1D][\uFF1D][\uFF1D][\uFF1D][\uFF1D][\uFF1D][\uFF1D]"
        val expected = arrayOf(
            emptyRank, emptyRank, emptyRank, emptyRank, emptyRank, emptyRank,
            "[\uFF1D][\uFF1D][\uFF1D][\uFF1D][\uFF1D][\uFF1D][\uFF1D][\u265F]",
            "[\u265C][\u265E][\u265D][\u265A][\u265B][\u265D][\u265E][\u265C]"
        )

        assertEquals(8, result.size)

        for(i in result.indices) {
            assertEquals(result[i], expected[i])
        }
    }
}