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

        if (fightPlayer.freeze == 1)
        {
            Random r = new Random();
            // Kijk of freeze ophoud
            if (r.nextInt(10)+1 == 1)
            {
                fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.ownerPokemon.pokemon.name} is no longer frozen."))

                fightPlayer.freeze = 0;
            }
            else
            {

                fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.ownerPokemon.pokemon.name} is frozen solid!"))

                Moves.setMove(fight,fightPlayer,null)
            }
        }
    }

    public static void freezeAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer, Move attackMove){

        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        // freeze
        if (moveInfo.freezeAction && moveInfo.effectSucces)
        {
            if (defendingFightPlayer.freeze == 0)
            {
                if (defendingOwnerPokemon.pokemon.type1 == "ice" || defendingOwnerPokemon.pokemon.type2 == "ice")
                {
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is immune to freeze."))

                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    defendingFightPlayer.freeze = 1;
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is frozen."))

                    moveInfo.freezeActionSucces = true
                }
            }
            else
            {
                if (attackMove.category == MoveCategory.StatusMove)
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is already frozen."))
            }
        }
    }

}
