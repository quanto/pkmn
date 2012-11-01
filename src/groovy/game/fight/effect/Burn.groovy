package game.fight.effect

import game.context.Fight
import game.context.MoveInfo
import game.context.FightPlayer
import game.OwnerPokemon
import game.fight.status.Recover
import game.fight.log.MessageLog
import game.Move

class Burn {

    public static void burnAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer, Move attackMove){
        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        // burn
        if (moveInfo.burnAction && moveInfo.effectSucces)
        {
            if (defendingFightPlayer.burn == 0)
            {
                if (defendingOwnerPokemon.pokemon.type1 == "fire" || defendingOwnerPokemon.pokemon.type2 == "fire")
                {
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is immune to fire."))
                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    defendingFightPlayer.burn = 1;

                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is burning."))
                    moveInfo.burnActionSucces = true;
                }
            }
            else
            {
                if (attackMove.category == "status move")
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is already burning."))
            }
        }
    }

}
