package game.fight.status

import game.Recover
import game.MoveInfo
import game.Fight
import game.FightPlayer
import game.OwnerPokemon

class Poison {

    public static void poisonAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer){
        // Poison
        if (moveInfo.poisonAction)
        {
            OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

            // controlleer of de status al staat
            if (defendingFightPlayer.poison == 0 && defendingFightPlayer.badlypoisond == 0 && moveInfo.effectSucces)
            {
                if (defendingOwnerPokemon.pokemon.type1 == "poison" || defendingOwnerPokemon.pokemon.type2== "poison" || defendingOwnerPokemon.pokemon.type1 == "steel" || defendingOwnerPokemon.pokemon.type2 == "steel")
                {
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is immune to poison.;";
                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    defendingFightPlayer.poison = 1
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is poisoned.;";
                    moveInfo.poisonActionSucces = true;
                }
            }
            else
            {
                // Bericht bij move die bedoelt is om te poisenen
                if (moveInfo.attackMoveeffectProb == 0 || moveInfo.attackMoveeffectProb == 100)
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already poisoned.;";
            }
        }
    }

    public static void badlyPoisondAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer){
        //badlypoisend wordt meer met iedere beurt
        if (moveInfo.badlypoisondAction)
        {
            OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

            // kijk of niet al poisond
            if (defendingFightPlayer.poison == 0 && defendingFightPlayer.badlypoisond == 0 && moveInfo.effectSucces)
            {
                if (defendingOwnerPokemon.pokemon.type1 == "poison" || defendingOwnerPokemon.pokemon.type2 == "poison" || defendingOwnerPokemon.pokemon.type1 == "steel" || defendingOwnerPokemon.pokemon.type2 == "steel")
                {
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is immune to poison.;";
                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    // Zet de status
                    defendingFightPlayer.badlypoisond = 1;
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is badly poisoned.;";
                    moveInfo.badlypoisondActionSucces = true;
                }
            }
            else
            {
                // Bericht bij move die bedoelt is om te poisenen
                if (moveInfo.attackMoveeffectProb == 0 || moveInfo.attackMoveeffectProb == 100)
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already poisoned.;";
            }
        }
    }

}
