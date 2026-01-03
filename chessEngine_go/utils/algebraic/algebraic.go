package algebraic

import (
	"log"
	"strings"
)

func ToAlgebraic(index int) string {
	var file = index%8 + 65
	var rank = index/8 + 49

	if file < 65 || file > 72 || rank < 49 || rank > 56 {
		log.Fatal("Index is not within range cannot be converted to Algebraic Notation")
	}

	var fileAlgebraic = rune(file)
	var rankAlgebraic = rune(rank)

	return string(fileAlgebraic) + string(rankAlgebraic)
}

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

/*Split returns algebraic notation (string) of two chess moves as two separate strings in an array*/
func Split(algebraic string) []string {
	if len(algebraic) > 4 {
		log.Fatal("Split can only split strings of length 4")
	}

	return []string{string(algebraic[0]) + string(algebraic[1]),
		string(algebraic[2]) + string(algebraic[3])}
}
