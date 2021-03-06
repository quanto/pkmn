package game.fight.status

import game.fight.log.MessageLog
import game.context.Fight
import game.context.FightPlayer

class Accuraccy {

    /**
     *
     */
    public static void addToAccuracyStage(Fight fight, int addToStage, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer)
    {
        if (addToStage > 0)
        {
            // Max 6
            if (attackingFightPlayer.fightPokemon.accuracyStage < 6)
            {
                attackingFightPlayer.fightPokemon.accuracyStage += addToStage
                fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.fightPokemon.name}`s accuracy raises"))
            }
        }
        else
        {
            // min -6
            if (defendingFightPlayer.fightPokemon.accuracyStage> -6)
            {
                defendingFightPlayer.fightPokemon.accuracyStage += addToStage
                fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.fightPokemon.name}`s accuracy lowers."))
            }
        }
    }

    public static double getAccuracy(int accuraccy, int stage)
    {
        double p = 0
        if (stage == -6)
        {
            p = 0.33;
        }
        else if (stage == -5)
        {
            p = 0.375;
        }
        else if (stage == -4)
        {
            p = 0.428;
        }
        else if (stage == -3)
        {
            p = 0.5;
        }
        else if (stage == -2)
        {
            p = 0.6;
        }
        else if (stage == -1)
        {
            p = 0.75;
        }
        else if (stage == 0)
        {
            p = 1;
        }
        else if (stage == 1)
        {
            p = 1.33;
        }
        else if (stage == 2)
        {
            p = 1.66;
        }
        else if (stage == 3)
        {
            p = 2;
        }
        else if (stage == 4)
        {
            p = 2.33;
        }
        else if (stage == 5)
        {
            p = 2.66;
        }
        else if (stage == 6)
        {
            p = 3;
        }
        return p * accuraccy;
    }

}
