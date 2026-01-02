package algebraic

import (
	"strings"
)

func IsInAlgebraic(input string) bool {
	for pos, char := range input {
		if pos%2 == 0 {
			if char < 'a' || char > 'h' {
				return false
			}
		} else {
			if char < '1' || char > '8' {
				return false
			}
		}
	}
	return true
}

func ToIndex(algebraic string) int {
	var normed = strings.ToLower(algebraic)
	var file = normed[0] - 97
	var rank = normed[1] - 49
	return int(rank*8 + file)
}
