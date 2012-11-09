package game.fight.effect

import game.fight.log.MessageLog
import game.fight.status.Recover
import game.context.Fight
import game.context.FightPlayer
import game.Moves
import game.context.MoveInfo
import game.fight.action.NoAction

class Confusion {

    public static void checkConfusion(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.fightPokemon.confusion > 0)
        {
            // Geef bericht
            if (fightPlayer.fightPokemon.confusion == 1)
            {
                fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.fightPokemon.name} is no longer confused."))
                fightPlayer.fightPokemon.confusion = 0
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
                    fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.fightPokemon.name} is confused."))

                    // verlaag beurten
                    fightPlayer.fightPokemon.confusion -= 1;
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
            if (defendingFightPlayer.fightPokemon.confusion > 0)
            {
                fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.fightPokemon.name} is already confused."))
            }
            // confusion
            else
            {
                Recover.removeAllStatusAfflictions(defendingFightPlayer);
                defendingFightPlayer.fightPokemon.confusion = random.nextInt(3)+2;
                fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name  + " became confused."))

                moveInfo.confusionActionSucces = true
                if (moveInfo.effectActionOnBoth)
                {
                    Recover.removeAllStatusAfflictions(attackFightPlayer);
                    attackFightPlayer.fightPokemon.confusion = random.nextInt(3)+2;
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name  + " became confused."))
                }
            }
        }
    }

}
