package game.fight.effect

import game.fight.action.NoAction
import game.fight.log.MessageLog
import game.context.Fight
import game.context.FightPlayer
import game.Moves
import game.context.MoveInfo
import game.fight.status.Recover
import game.Move
import game.context.MoveCategory

class Paralyses {

    public static void checkParalyses(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.fightPokemon.paralysis == 1)
        {
            Random r = new Random();
            // Kijk of effected door paralysis
            if (r.nextInt(4)+1 == 2)
            {
                fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.fightPokemon.name} is paralyzed. It can`t move."))

                // geen move
                Moves.setMove(fight,fightPlayer,new NoAction())
            }
        }
    }

    public static void paralysisAction(Fight fight, MoveInfo moveInfo, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer, Move attackMove){
        // paralysis
        if (moveInfo.paralysisAction && moveInfo.effectSucces)
        {
            if (defendingFightPlayer.fightPokemon.paralysis == 0)
            {
                Recover.removeAllStatusAfflictions(defendingFightPlayer);
                defendingFightPlayer.fightPokemon.paralysis = 1;
                fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is paralyzed."))

                moveInfo.paralysisActionSucces = true;
            }
            else
            {
                if (attackMove.category == MoveCategory.StatusMove)
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is already paralyzed."))
            }
        }
    }

}
