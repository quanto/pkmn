package game.fight.effect

import game.fight.log.MessageLog
import game.fight.status.Recover
import game.context.Fight
import game.context.FightPlayer
import game.Moves
import game.context.MoveInfo
import game.fight.action.NoAction

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 08:57
 * To change this template use File | Settings | File Templates.
 */
class Confusion {

    public static void checkConfusion(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.confusion > 0)
        {
            // Geef bericht
            if (fightPlayer.confusion == 1)
            {
                fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.ownerPokemon.pokemon.name} is no longer confused."))
                fightPlayer.confusion = 0
//                if (player != 2 || fight.battleType != "pve")
//                {
                    // :TODO what to do
//                    fight.update();
//                    header("Location: index.php");
//                }
            }
            else
            {
                Random r = new Random()
                // 50 % kans dat geen aanval gedaan kan worden
                if (r.nextInt(2) == 1)
                {
                    fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.ownerPokemon.pokemon.name} is confused."))

                    // verlaag beurten
                    fightPlayer.confusion -= 1;
                    // geen move
                    Moves.setMove(fight,fightPlayer,new NoAction())
                }
            }
        }
    }

    public static void confusionAction(Fight fight, MoveInfo moveInfo, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer){
        Random random = new Random()
        // confusion
        if (moveInfo.confusionAction && moveInfo.effectSucces)
        {
            // kijk of de tegenstander confusion is
            if (defendingFightPlayer.confusion > 0)
            {
                fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.ownerPokemon.pokemon.name} is already confused."))
            }
            // confusion
            else
            {
                Recover.removeAllStatusAfflictions(defendingFightPlayer);
                defendingFightPlayer.confusion = random.nextInt(3)+2;
                fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.ownerPokemon.pokemon.name  + " became confused."))

                moveInfo.confusionActionSucces = true
                if (moveInfo.effectActionOnBoth)
                {
                    Recover.removeAllStatusAfflictions(attackFightPlayer);
                    attackFightPlayer.confusion = random.nextInt(3)+2;
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.ownerPokemon.pokemon.name  + " became confused."))
                }
            }
        }
    }

}