package movement

type Type int

const (
	Move Type = iota
	Attack
	Rochade
)

var pieceName = map[Type]string{
	Move:    "Move",
	Attack:  "Attack",
	Rochade: "Rochade",
}

func (pt Type) String() string {
	return pieceName[pt]
}
