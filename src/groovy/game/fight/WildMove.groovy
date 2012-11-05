package game.fight

import game.context.FightPlayer
import game.Move
import game.LearnableMove
import game.fight.action.MoveAction
import game.fight.action.BattleAction

class WildMove {

    public static BattleAction choseWildMove(FightPlayer fightPlayer)
    {
        List<LearnableMove> learnableMoveList = LearnableMove.findAllByPokemonAndLearnLevelLessThanEquals(fightPlayer.ownerPokemon.pokemon,fightPlayer.level).findAll{ it.move.implemented }
        if (learnableMoveList){
            Collections.shuffle(learnableMoveList)
            return new MoveAction(move: learnableMoveList.last().move, ownerMoveForPP:null)
        }
        else {
            return new MoveAction(move: Move.findByName("Struggle"), ownerMoveForPP:null)
        }
    }

}
