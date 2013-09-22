package game.fight.effect

import game.fight.action.NoAction
import game.fight.log.MessageLog
import game.fight.status.Recover
import game.context.Fight
import game.context.FightPlayer
import game.Moves
import game.context.MoveInfo

class Sleep {

    public static void checkSleep(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.fightPokemon.sleep > 0)
        {
            // Geef bericht
            if (fightPlayer.fightPokemon.sleep == 1)
            {
                fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.fightPokemon.name} wakes up."))

                fight.log += "m:;"
                fightPlayer.fightPokemon.sleep = 0
            }
            else
            {
                fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.fightPokemon.name} is a sleep."))

                // verlaag slaap beurten
                fightPlayer.fightPokemon.sleep -= 1

                // geen move
                Moves.setMove(fight,fightPlayer,new NoAction())
            }
        }
    }

    public static void sleepAction(Fight fight, MoveInfo moveInfo, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer){
        Random random = new Random()
        if (moveInfo.sleepAction && moveInfo.effectSucces)
        {
            // kijk of de tegenstander al niet slaapt
            if (defendingFightPlayer.fightPokemon.sleep > 0)
            {
                fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.fightPokemon.name} is already asleep."))

            }
            // laat de tegenstander slapen
            else
            {
                Recover.removeAllStatusAfflictions(defendingFightPlayer);
                defendingFightPlayer.fightPokemon.sleep = random.nextInt(6)+2
                fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.fightPokemon.name} fell asleep."))

                moveInfo.sleepActionSucces = true;
            }
        }
    }
    
}
