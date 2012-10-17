package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 08:43
 * To change this template use File | Settings | File Templates.
 */
class Paralyses {

    public static void checkParalyses(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.paralysis == 1)
        {
            Random r = new Random();
            // Kijk of effected door paralysis
            if (r.nextInt(4)+1 == 2)
            {
                fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} is paralyzed. It can`t move.;";
                // geen move
                Moves.setMove(fight,fightPlayer,null)
            }
        }
    }

    public static void paralysisAction(Fight fight, MoveInfo moveInfo, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer){
        // paralysis
        if (moveInfo.paralysisAction && moveInfo.effectSucces)
        {
            if (defendingFightPlayer.paralysis == 0)
            {
                Recover.removeAllStatusAfflictions(defendingFightPlayer);
                defendingFightPlayer.paralysis = 1;
                fight.log += "m:" + defendingFightPlayer.ownerPokemon.pokemon.name + " is paralyzed.;";
                moveInfo.paralysisActionSucces = true;
            }
            else
            {
                if (moveInfo.attackMoveeffectProb == 0 || moveInfo.attackMoveeffectProb == 100)
                    fight.log += "m:" + defendingFightPlayer.ownerPokemon.pokemon.name + " is already paralyzed.;";
            }
        }
    }

}
