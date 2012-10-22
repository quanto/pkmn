package game.fight

import game.Move
import game.OwnerPokemon

class NPCHelper {

    public static Move choseNpcMove(OwnerPokemon ownerPokemon)
    {
        List<Move> moves = ownerPokemon.ownerMoves.collect { it.move }

        if (moves){
            Collections.shuffle(moves)
            return moves.last()
        }
        else {
            return Move.findByName("Struggle")
        }

    }
    
}
