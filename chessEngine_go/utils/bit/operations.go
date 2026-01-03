package bit

import "fmt"

var Empty uint64 = 0x0
var Universe uint64 = 0xffffffffffffffff

/*GetBit returns the bit value at the given index from the given board*/
func GetBit(bitLookup uint64, bitIndex int) bool {
	var shift = 63 - bitIndex
	bitLookup = bitLookup >> shift
	bitLookup &= -bitLookup
	return bitLookup == 1
}

/*FlipBits flips the bit value in the given bitboard at the given indices and returns the new board*/
func FlipBits(board uint64, indices []int) uint64 {
	for _, index := range indices {
		board = FlipBit(board, index)
	}
	return board
}

/*FlipBit flips the bit value in the bitboard at the given index and returns the new board*/
func FlipBit(board uint64, index int) uint64 {
	var shift = 63 - index
	var bitMask uint64 = 1 << shift
	board = board ^ bitMask // Turn bit B on
	return board
}

/*SwapBit swaps two bits in a bitboard*/
func SwapBit(board uint64, bitIndex int, targetIndex int) uint64 {
	var shift = 63 - bitIndex
	var bitMask = 1<<shift ^ Universe
	board = board & bitMask // Turn bit A off

	shift = 63 - targetIndex
	bitMask = 1 << shift
	board = board | bitMask // Turn bit B on
	return board
}

/*PrintBitDebug for debugging, prints the given bitboard as a readable 8x8 board*/
func PrintBitDebug(bit uint64, prefixTxt string) {
	fmt.Printf(prefixTxt, "%64b", bit)
}
