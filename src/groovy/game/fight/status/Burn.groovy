package game.fight.status

import game.Fight
import game.MoveInfo
import game.FightPlayer
import game.OwnerPokemon
import game.Recover

class Burn {

    public static void burnAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer){
        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        // burn
        if (moveInfo.burnAction && moveInfo.effectSucces)
        {
            if (defendingFightPlayer.burn == 0)
            {
                if (defendingOwnerPokemon.pokemon.type1 == "fire" || defendingOwnerPokemon.pokemon.type2 == "fire")
                {
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name  + " is immune to fire.;";
                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    defendingFightPlayer.burn = 1;
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name  + " is burning.;";
                    moveInfo.burnActionSucces = true;
                }
            }
            else
            {
                if (moveInfo.attackMoveeffectProb == 0 || moveInfo.attackMoveeffectProb == 100)
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already burning.;";
            }
        }
    }

}
