package game.fight

import game.context.FightPlayer
import game.Move
import game.LearnableMove

class WildMove {

    public static Move choseWildMove(FightPlayer fightPlayer)
    {
        List<LearnableMove> learnableMoveList = LearnableMove.findAllByPokemonAndLearnLevelLessThanEquals(fightPlayer.ownerPokemon.pokemon,fightPlayer.level).findAll{ it.move.implemented }
        if (learnableMoveList){
            Collections.shuffle(learnableMoveList)
            return learnableMoveList.last().move
        }
        else {
            return Move.get(394) // Geen move, struggle
        }
    }

}
