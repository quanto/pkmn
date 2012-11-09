package game.fight.effect

import game.fight.status.Recover
import game.context.MoveInfo
import game.context.Fight
import game.context.FightPlayer
import game.OwnerPokemon
import game.fight.log.MessageLog
import game.Move
import game.context.MoveCategory

class Poison {

    public static void poisonAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer, Move attackMove){
        // Poison
        if (moveInfo.poisonAction)
        {

            // controlleer of de status al staat
            if (defendingFightPlayer.fightPokemon.poison == 0 && defendingFightPlayer.fightPokemon.badlypoisond == 0 && moveInfo.effectSucces)
            {
                if (defendingFightPlayer.fightPokemon.hasType("poison") || defendingFightPlayer.fightPokemon.hasType("steel"))
                {
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is immune to poison."))
                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    defendingFightPlayer.fightPokemon.poison = 1
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is poisoned."))
                    moveInfo.poisonActionSucces = true;
                }
            }
            else
            {
                // Bericht bij move die bedoelt is om te poisenen
                if (attackMove.category == MoveCategory.StatusMove)
                fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is already poisoned."))
            }
        }
    }

    public static void badlyPoisondAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer, Move attackMove){
        //badlypoisend wordt meer met iedere beurt
        if (moveInfo.badlypoisondAction)
        {

            // kijk of niet al poisond
            if (defendingFightPlayer.fightPokemon.poison == 0 && defendingFightPlayer.fightPokemon.badlypoisond == 0 && moveInfo.effectSucces)
            {
                if (defendingFightPlayer.fightPokemon.hasType("poison") || defendingFightPlayer.fightPokemon.hasType("steel"))
                {
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is immune to poison."))
                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    // Zet de status
                    defendingFightPlayer.fightPokemon.badlypoisond = 1;
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " badly poisoned."))
                    moveInfo.badlypoisondActionSucces = true;
                }
            }
            else
            {
                // Bericht bij move die bedoelt is om te poisenen
                if (attackMove.category == MoveCategory.StatusMove)
                    fight.roundResult.battleActions.add(new MessageLog(defendingFightPlayer.fightPokemon.name + " is already poisoned."))
            }
        }
    }

}
