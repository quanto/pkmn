package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 20:16
 * To change this template use File | Settings | File Templates.
 */
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
