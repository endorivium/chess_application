package piece

type MovementType int

const (
	Move MovementType = iota
	Attack
)

var pieceName = map[MovementType]string{
	Move:   "Move",
	Attack: "Attack",
}

func (pt MovementType) String() string {
	return pieceName[pt]
}
