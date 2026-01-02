package movement

type Finder interface {
	FindMoves(index int, board uint64) uint64
	FindAttacks(index int, allies uint64, enemies uint64) uint64
	FindAllAttacks(index int, board uint64, allies uint64, enemies uint64) uint64
}
