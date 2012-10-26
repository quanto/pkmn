package game

import game.fight.MessageAction

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 09:04
 * To change this template use File | Settings | File Templates.
 */
class Sleep {

    public static void checkSleep(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.sleep > 0)
        {
            // Geef bericht
            if (fightPlayer.sleep == 1)
            {
                fight.roundResult.battleActions.add(new MessageAction("${fightPlayer.ownerPokemon.pokemon.name} wakes up."))

                fight.log += "m:;"
                fightPlayer.sleep = 0
            }
            else
            {
                fight.roundResult.battleActions.add(new MessageAction("${fightPlayer.ownerPokemon.pokemon.name} is a sleep."))

                // verlaag slaap beurten
                fightPlayer.sleep -= 1

                // geen move
                Moves.setMove(fight,fightPlayer,null)
            }
        }
    }

    public static void sleepAction(Fight fight, MoveInfo moveInfo, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer){
        Random random = new Random()
        if (moveInfo.sleepAction && moveInfo.effectSucces)
        {
            // kijk of de tegenstander al niet slaapt
            if (defendingFightPlayer.sleep > 0)
            {
                fight.roundResult.battleActions.add(new MessageAction("${defendingFightPlayer.ownerPokemon.pokemon.name} is already asleep."))

            }
            // laat de tegenstander slapen
            else
            {
                Recover.removeAllStatusAfflictions(defendingFightPlayer);
                defendingFightPlayer.sleep = random.nextInt(6)+2
                fight.roundResult.battleActions.add(new MessageAction("${defendingFightPlayer.ownerPokemon.pokemon.name} fell asleep."))

                moveInfo.sleepActionSucces = true;
            }
        }
    }
    
}
