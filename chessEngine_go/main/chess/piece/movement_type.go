package piece

type MovementType int

const (
	Move MovementType = iota
	Attack
	Rochade
)

var pieceName = map[MovementType]string{
	Move:    "Move",
	Attack:  "Attack",
	Rochade: "Rochade",
}

func (pt MovementType) String() string {
	return pieceName[pt]
}
