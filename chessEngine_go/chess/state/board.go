package state

import (
	"errors"
	"log"
	"math/bits"

	"main.go/chess/data"
	"main.go/chess/piece"
	"main.go/chess/rules"
	"main.go/utils/bit"
	"main.go/utils/chessboard"
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
	return &BoardState{boards: boards, ruleBook: ruleBook}
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
		default:
			bs.execAttack(move.Piece, move, whiteTurn)
		}

		bs.recordMove(move)
		return true
	}
	return false
}

/*getColorPieceAt returns the chess piece at the given index but only if it's the corresponding color*/
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

func (bs *BoardState) recordMove(move data.ChessMove) {
	bs.moveHistory = append(bs.moveHistory, move)
}

/*IsCheckmate checks if the king of the current player is in checkmate*/
func (bs *BoardState) IsCheckmate(whiteTurn bool) bool {
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

	return bs.IsCheck(whiteTurn) && bits.OnesCount64(availableSquares) == 0
}

/*getKingPerimeter returns indices of squares surrounding the king at the given index*/
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

/*getAllyOccupiedSquares returns bitboard with all squares that are occupied by allies*/
func (bs *BoardState) getAllyOccupiedSquares(indices []int, isWPlayer bool) uint64 {
	var allyBoard = bs.getColorBoard(isWPlayer)
	return bit.FlipBits(bit.Empty, indices) & allyBoard
}

/*IsCheck checks if the king of the current player is in check*/
func (bs *BoardState) IsCheck(whiteTurn bool) bool {
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

/*getThreatenedSquares returns bitboard of all enemy threatened squares within the given indices array*/
func (bs *BoardState) getThreatenedSquares(indices []int, isWPlayer bool) uint64 {
	var threatened = bit.Empty
	for _, index := range indices {
		if bs.isSquareThreatened(index, isWPlayer) {
			threatened = threatened ^ bit.FlipBit(bit.Empty, index)
		}
	}
	return threatened
}

/*AreSquaresThreatened returns true if all squares are threatened by enemy pieces*/
func (bs *BoardState) AreSquaresThreatened(indices []int, isWPlayer bool) bool {
	for _, index := range indices {
		if bs.isSquareThreatened(index, isWPlayer) {
			return true
		}
	}
	return false
}

/*isSquareThreatened returns if the given square is threatened by any enemy pieces (done through simulation)*/
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

/*simulateAtk simulate an attack with the given indices*/
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

/*getPieceAt returns chess piece at the given index, color is irrelevant*/
func (bs *BoardState) getPieceAt(index int) (error, data.PieceType) {
	var pieceBit = bit.FlipBit(bit.Empty, index)
	for i, board := range bs.boards {
		if bits.OnesCount64(pieceBit&board) >= 1 {
			return nil, data.PieceType(i)
		}
	}
	return errors.New("there was no piece at the given index"), data.WBishop
}

/*getPieceBoard overload of above, return bitboard via index*/
func (bs *BoardState) getPieceBoard(index int) uint64 {
	return bs.boards[index]
}

/*findPossibleMoves find possible squares that the chess piece at the given index (if there is one) can movement to*/
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

/*areSquaresOccupied returns true if all squares at the given indices are occupied by other pieces*/
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
