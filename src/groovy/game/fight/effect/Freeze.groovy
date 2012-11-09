package game.fight.effect

import game.fight.log.MessageLog
import game.context.Fight
import game.context.FightPlayer
import game.Moves
import game.context.MoveInfo
import game.OwnerPokemon
import game.fight.status.Recover
import game.Move
import game.context.MoveCategory

class Freeze {

    public static void checkFreeze(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.fightPokemon.freeze == 1)
        {
            Random r = new Random();
            // Kijk of freeze ophoud
            if (r.nextInt(10)+1 == 1)
            {
                fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.fightPokemon.name} is no longer frozen."))

                fightPlayer.fightPokemon.freeze = 0;
            }
            else
            {

                fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.fightPokemon.name} is frozen solid!"))

                Moves.setMove(fight,fightPlayer,null)
            }
        }
    }

    public static void freezeAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer, Move attackMove){


        // freeze
        if (moveInfo.freezeAction && moveInfo.effectSucces)
        {
            if (defendingFightPlayer.fightPokemon.freeze == 0)
            {
                if (defendingFightPlayer.fightPokemon.hasType("ice"))
                {
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is immune to freeze."))

                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    defendingFightPlayer.fightPokemon.freeze = 1;
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is frozen."))

                    moveInfo.freezeActionSucces = true
                }
            }
            else
            {
                if (attackMove.category == MoveCategory.StatusMove)
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is already frozen."))
            }
        }
    }

}
