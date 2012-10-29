package game.fight

import game.Move
import game.OwnerPokemon
import game.fight.action.BattleAction
import game.fight.action.MoveAction

class NPCHelper {

    public static BattleAction choseNpcMove(OwnerPokemon ownerPokemon)
    {
        List<Move> moves = ownerPokemon.ownerMoves.collect { it.move }

        if (moves){
            Collections.shuffle(moves)
            return new MoveAction(move: moves.last())
        }
        else {
            return new MoveAction(move: Move.findByName("Struggle"))
        }

    }
    
}
