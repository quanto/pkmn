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
            OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

            // controlleer of de status al staat
            if (defendingFightPlayer.poison == 0 && defendingFightPlayer.badlypoisond == 0 && moveInfo.effectSucces)
            {
                if (defendingOwnerPokemon.pokemon.type1 == "poison" || defendingOwnerPokemon.pokemon.type2== "poison" || defendingOwnerPokemon.pokemon.type1 == "steel" || defendingOwnerPokemon.pokemon.type2 == "steel")
                {
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is immune to poison."))
                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    defendingFightPlayer.poison = 1
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is poisoned."))
                    moveInfo.poisonActionSucces = true;
                }
            }
            else
            {
                // Bericht bij move die bedoelt is om te poisenen
                if (attackMove.category == MoveCategory.StatusMove)
                fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is already poisoned."))
            }
        }
    }

    public static void badlyPoisondAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer, Move attackMove){
        //badlypoisend wordt meer met iedere beurt
        if (moveInfo.badlypoisondAction)
        {
            OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

            // kijk of niet al poisond
            if (defendingFightPlayer.poison == 0 && defendingFightPlayer.badlypoisond == 0 && moveInfo.effectSucces)
            {
                if (defendingOwnerPokemon.pokemon.type1 == "poison" || defendingOwnerPokemon.pokemon.type2 == "poison" || defendingOwnerPokemon.pokemon.type1 == "steel" || defendingOwnerPokemon.pokemon.type2 == "steel")
                {
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is immune to poison."))
                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    // Zet de status
                    defendingFightPlayer.badlypoisond = 1;
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " badly poisoned."))
                    moveInfo.badlypoisondActionSucces = true;
                }
            }
            else
            {
                // Bericht bij move die bedoelt is om te poisenen
                if (attackMove.category == MoveCategory.StatusMove)
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is already poisoned."))
            }
        }
    }

}
