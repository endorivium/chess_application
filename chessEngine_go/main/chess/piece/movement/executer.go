package movement

import "chessEngine_go/chess/data"

type Checker interface {
	CanExecuteMove(move data.ChessMove, board uint64, allies uint64, enemies uint64, simulated bool) (bool, Type)
}
