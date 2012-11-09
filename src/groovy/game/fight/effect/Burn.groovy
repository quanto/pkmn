package game.fight.effect

import game.context.Fight
import game.context.MoveInfo
import game.context.FightPlayer
import game.OwnerPokemon
import game.fight.status.Recover
import game.fight.log.MessageLog
import game.Move
import game.context.MoveCategory

class Burn {

    public static void burnAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer, Move attackMove){

        // burn
        if (moveInfo.burnAction && moveInfo.effectSucces)
        {
            if (defendingFightPlayer.fightPokemon.burn == 0)
            {
                if (defendingFightPlayer.fightPokemon.hasType("fire"))
                {
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is immune to fire."))
                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    defendingFightPlayer.fightPokemon.burn = 1;

                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is burning."))
                    moveInfo.burnActionSucces = true;
                }
            }
            else
            {
                if (attackMove.category == MoveCategory.StatusMove)
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is already burning."))
            }
        }
    }

}
