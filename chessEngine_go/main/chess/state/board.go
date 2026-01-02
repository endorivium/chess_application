package state

import (
	"chessEngine_go/chess/data"
	"chessEngine_go/chess/piece"
	"chessEngine_go/chess/rules"
	"chessEngine_go/utils/bit"
	"chessEngine_go/utils/chessboard"
	"errors"
	"log"
	"math/bits"
)

type BoardState struct {
	boards      []uint64
	moveHistory []data.ChessMove
	ruleBook    rules.RuleBook
}

func NewBoardState() *BoardState {
	var ruleBook = rules.NewRuleBook()
	var boards = []uint64{0x2400000000000000, 0x800000000000000, 0x4200000000000000,
		0xFF000000000000, 0x1000000000000000, 0x8100000000000000,
		0x24, 0x8, 0x42, 0xFF00, 0x10, 0x81,
	}
	return &BoardState{boards: boards, ruleBook: *ruleBook}
}

func (bs *BoardState) Initialize() {
	bs.boards = []uint64{0x2400000000000000, 0x800000000000000, 0x4200000000000000,
		0xFF000000000000, 0x1000000000000000, 0x8100000000000000,
		0x24, 0x8, 0x42, 0xFF00, 0x10, 0x81,
	}
}

func (bs *BoardState) execChessMove(move data.ChessMove, whiteTurn bool) bool {
	var err error
	err, move.Piece = bs.getColorPieceAt(move.Initial, whiteTurn)

	if err != nil {
		return false
	}

	var pieceRule = bs.ruleBook.GetRule(move.Piece)

	var ok, moveExec = pieceRule.CanExecuteMove(move, bs.GetBoardState(),
		bs.getColorBoard(whiteTurn), bs.getColorBoard(!whiteTurn), false)
	if ok {
		switch moveExec {
		case piece.Move:
			bs.execMove(move.Piece, move)
		case piece.Attack:
			bs.execAttack(move.Piece, move, whiteTurn)
		default:
			bs.execRochade(move.Piece, move)
		}

		//bs.checkKingRooksMoved(move.Piece, move.Initial)
		bs.recordMove(move)
		return true
		//if bs.pawnTransform {
		//	return true, &move
		//} else {
		//	return true, nil
		//}
	}
	return false
}

/*returns the chess piece at the given index but only if it's the corresponding color*/
func (bs *BoardState) getColorPieceAt(index int, isWhite bool) (error, data.PieceType) {
	var pieceBit = bit.FlipBit(bit.Empty, index)

	for i, board := range bs.boards {
		if bits.OnesCount64(pieceBit&board) >= 1 {
			if chessboard.IsWhite(data.PieceType(i)) == isWhite {
				return nil, data.PieceType(i)
			}
		}
	}
	return errors.New("there was no piece at the given index in the correct color"), data.BBishop
}

/*GetBoardState returns the current state of the board*/
func (bs *BoardState) GetBoardState() uint64 {
	var board = bit.Empty

	for _, pieceBoard := range bs.boards {
		board = board ^ pieceBoard
	}
	return board
}

/*getColorBoard returns current board of the given color*/
func (bs *BoardState) getColorBoard(isWhite bool) uint64 {
	var board = bit.Empty

	if isWhite {
		for i := range 5 {
			board = board ^ bs.getPieceBoardByEnum(data.PieceType(i))
		}
	} else {
		for i := 6; i <= 11; i++ {
			board = board ^ bs.getPieceBoardByEnum(data.PieceType(i))
		}
	}
	return board
}

/*getPieceBoardByEnum returns the bitboard of the given chess piece*/
func (bs *BoardState) getPieceBoardByEnum(piece data.PieceType) uint64 {
	return bs.boards[piece]
}

/*GetPieceBoards return array of all chess piece bitboards*/
func (bs *BoardState) GetPieceBoards() []uint64 {
	pieceBoards := make([]uint64, 12)

	for i, board := range bs.boards {
		pieceBoards[i] = board
	}

	return pieceBoards
}

func (bs *BoardState) execMove(piece data.PieceType, move data.ChessMove) {
	bs.boards[piece] = bit.SwapBit(bs.boards[piece], move.Initial, move.Target)
}

func (bs *BoardState) execAttack(piece data.PieceType, move data.ChessMove, whiteTurn bool) {
	var err, enemy = bs.getColorPieceAt(move.Target, !whiteTurn)

	if err != nil {
		log.Fatal(err)
	}

	bs.boards[piece] = bit.SwapBit(bs.boards[piece], move.Initial, move.Target)
	bs.boards[enemy] = bit.FlipBit(bs.boards[enemy], move.Target)
}

func (bs *BoardState) execRochade(piece data.PieceType, move data.ChessMove) {
	bs.boards[piece] = bit.SwapBit(bs.boards[piece], move.Initial, move.Target)

	//short rochade (moving right)
	if move.Initial < move.Target {
		if chessboard.IsWhite(piece) {
			bs.boards[5] = bit.SwapBit(bs.boards[5], move.Initial+3, move.Target-1)
		} else {
			bs.boards[11] = bit.SwapBit(bs.boards[11], move.Initial+3, move.Target-1)
		}
	} else { //long rochade (moving left)
		if chessboard.IsWhite(piece) {
			bs.boards[5] = bit.SwapBit(bs.boards[5], move.Initial-4, move.Target+1)
		} else {
			bs.boards[11] = bit.SwapBit(bs.boards[11], move.Initial-4, move.Target+1)
		}
	}
}

//
///*checkKingRooksMoved checks if any kings or rooks have moved and flips boolean if true*/
//func (bs *BoardState) checkKingRooksMoved(piece data.PieceType, index int) {
//	switch piece {
//	case data.BKing:
//		bs.bKingMoved = true
//	case data.WKing:
//		bs.wKingMoved = true
//	case data.BRook:
//		if index == 0 {
//			bs.bLeftRookMoved = true
//		}
//		if index == 7 {
//			bs.bRightRookMoved = true
//		}
//
//	case data.WRook:
//		if index == 56 {
//			bs.wLeftRookMoved = true
//		}
//		if index == 63 {
//			bs.wRightRookMoved = true
//		}
//	default:
//		return
//	}
//}

func (bs *BoardState) recordMove(move data.ChessMove) {
	bs.moveHistory = append(bs.moveHistory, move)
}

///*NotifyPawnTransformation called from pawn chess piece to notify future execution of pawn transformation at end of movement execution*/
//func (bs *BoardState) NotifyPawnTransformation() {
//	bs.pawnTransform = true
//}
//
///*execPawnTransformation deletes pawn and puts chosen chess piece on the corresponding board*/
//func (bs *BoardState) execPawnTransformation(move data.ChessMove, transform data.PieceType) {
//	bs.boards[move.Piece] = bit.FlipBit(bs.boards[move.Piece], move.Target)
//	bs.boards[transform] = bit.FlipBit(bs.boards[transform], move.Target)
//	bs.pawnTransform = false
//}

/*checks if the king of the current player is in checkmate*/
func (bs *BoardState) isCheckmate(whiteTurn bool) bool {
	var boardIndex = 1
	if !whiteTurn {
		boardIndex = 7
	}
	var kingBoard = bs.getPieceBoard(boardIndex)
	var kingIndex = chessboard.GetBoardIndices(kingBoard)

	if len(kingIndex) == 0 || len(kingIndex) != 1 {
		log.Fatal("There was either no or more than one King on the board!")
	}

	var kingPerimeter = bs.getKingPerimeter(kingIndex[0])
	var bitPerimeter = bit.FlipBits(bit.Empty, kingPerimeter)
	var threatenedSquares = bs.getThreatenedSquares(kingPerimeter, whiteTurn)
	var occupied = bs.getAllyOccupiedSquares(kingPerimeter, whiteTurn)

	//finds all squares that are neither occupied nor threatened by enemy pieces
	var availableSquares = (threatenedSquares ^ occupied) ^ bitPerimeter

	return bs.isCheck(whiteTurn) && bits.OnesCount64(availableSquares) == 0
}

/*returns indices of squares surrounding the king at the given index*/
func (bs *BoardState) getKingPerimeter(index int) []int {
	var perimeter []int
	for _, next := range chessboard.OmniDirectional {
		if chessboard.WillFileOverflow(index, index+next) || !chessboard.IsWithinBoard(index+next) {
			continue
		}
		perimeter = append(perimeter, index+next)
	}

	return perimeter
}

/*returns bitboard with all squares that are occupied by allies*/
func (bs *BoardState) getAllyOccupiedSquares(indices []int, isWPlayer bool) uint64 {
	var allyBoard = bs.getColorBoard(isWPlayer)
	return bit.FlipBits(bit.Empty, indices) & allyBoard
}

/*checks if the king of the current player is in check*/
func (bs *BoardState) isCheck(whiteTurn bool) bool {
	var boardIndex = 1
	if !whiteTurn {
		boardIndex = 7
	}
	var kingBoard = bs.getPieceBoard(boardIndex)
	var kingIndex = chessboard.GetBoardIndices(kingBoard)

	if len(kingIndex) == 0 || len(kingIndex) != 1 {
		log.Fatal("There was either no or more than one King on the board!")
	}

	return bs.isSquareThreatened(kingIndex[0], whiteTurn)
}

/*returns bitboard of all enemy threatened squares within the given indices array*/
func (bs *BoardState) getThreatenedSquares(indices []int, isWPlayer bool) uint64 {
	var threatened = bit.Empty
	for _, index := range indices {
		if bs.isSquareThreatened(index, isWPlayer) {
			threatened = threatened ^ bit.FlipBit(bit.Empty, index)
		}
	}
	return threatened
}

/*returns true if all squares are threatened by enemy pieces*/
func (bs *BoardState) areSquaresThreatened(indices []int, isWPlayer bool) bool {
	for _, index := range indices {
		if bs.isSquareThreatened(index, isWPlayer) {
			return true
		}
	}
	return false
}

/*returns if the given square is threatened by any enemy pieces (done through simulation)*/
func (bs *BoardState) isSquareThreatened(index int, isWPlayer bool) bool {
	var enemyBoard = bs.getColorBoard(!isWPlayer)

	//walk in all directions until enemy then check if it can attack square at index
	for _, step := range chessboard.OmniDirectional {
		var next = index
		for chessboard.IsWithinBoard(next + step) {
			if chessboard.WillFileOverflow(next, next+step) {
				break
			}

			next += step

			var enemy = bit.FlipBit(bit.Empty, next)
			enemy = enemy & enemyBoard
			if bits.OnesCount64(enemy) != 0 && bs.simulateAtk(next, index) {
				return true
			}
		}
	}

	//check if knights can attack square
	for _, step := range chessboard.KnightPattern {
		if chessboard.WillFileOverflow(index, index+step) || !chessboard.IsWithinBoard(index+step) {
			continue
		}
		var knight = bit.FlipBit(bit.Empty, index+step)
		knight = knight & enemyBoard
		if bits.OnesCount64(knight) != 0 {
			return true
		}
	}

	return false
}

/*simulate an attack with the given indices*/
func (bs *BoardState) simulateAtk(attacker int, target int) bool {
	var simulated = data.NewChessMoveByIndex(attacker, target)
	var err, chessPiece = bs.getPieceAt(attacker)

	if err != nil {
		log.Fatal(err)
	}

	var pieceRule = bs.ruleBook.GetRule(chessPiece)

	var white = chessboard.IsWhite(chessPiece)
	var simResult, _ = pieceRule.CanExecuteMove(*simulated, bs.GetBoardState(),
		bit.Empty, bs.getColorBoard(!white), true)
	return simResult
}

/*returns chess piece at the given index, color is irrelevant*/
func (bs *BoardState) getPieceAt(index int) (error, data.PieceType) {
	var pieceBit = bit.FlipBit(bit.Empty, index)
	for i, board := range bs.boards {
		if bits.OnesCount64(pieceBit&board) >= 1 {
			return nil, data.PieceType(i)
		}
	}
	return errors.New("there was no piece at the given index"), data.WBishop
}

/*overload of above, return bitboard via index*/
func (bs *BoardState) getPieceBoard(index int) uint64 {
	return bs.boards[index]
}

/*find possible squares that the chess piece at the given index (if there is one) can movement to*/
func (bs *BoardState) findPossibleMoves(move data.ChessMove, whiteTurn bool) (bool, data.PieceType, []int) {
	var err, chessPiece = bs.getColorPieceAt(move.Initial, whiteTurn)

	if err != nil {
		return false, data.BBishop, []int{}
	}

	var pieceRule = bs.ruleBook.GetRule(chessPiece)

	var possibleMoves = pieceRule.GetMoveSet(
		move.Initial, bs.GetBoardState(),
		bs.getColorBoard(whiteTurn), bs.getColorBoard(!whiteTurn))

	return true, chessPiece, chessboard.GetBoardIndices(possibleMoves.Move)
}

func (bs *BoardState) areSquaresOccupied(indices []int) bool {
	for _, index := range indices {

		var square = bit.FlipBit(bit.Empty, index)
		var occupied = square & bs.GetBoardState()

		if bits.OnesCount64(occupied) != 0 {
			return true
		}
	}
	return false
}

func (bs *BoardState) GetPrevMove() (bool, data.ChessMove) {
	if len(bs.moveHistory) == 0 {
		return false, data.ChessMove{}
	}

	return true, bs.moveHistory[len(bs.moveHistory)-1]
}
