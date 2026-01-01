package mock

import chessStateManagement.BoardStateManager

class MockBoardStateManager(): BoardStateManager() {
    fun initialize(mockBoards: Array<ULong>) {
        super.initialize()
        boards = mockBoards
    }
}