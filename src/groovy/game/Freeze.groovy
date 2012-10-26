package game

import game.fight.MessageAction

class Freeze {

    public static void checkFreeze(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.freeze == 1)
        {
            Random r = new Random();
            // Kijk of freeze ophoud
            if (r.nextInt(10)+1 == 1)
            {
                fight.roundResult.battleActions.add(new MessageAction("${fightPlayer.ownerPokemon.pokemon.name} is no longer frozen."))

                fightPlayer.freeze = 0;
            }
            else
            {

                fight.roundResult.battleActions.add(new MessageAction("${fightPlayer.ownerPokemon.pokemon.name} is frozen solid!"))

                Moves.setMove(fight,fightPlayer,null)
            }
        }
    }

    public static void freezeAction(Fight fight, MoveInfo moveInfo, FightPlayer defendingFightPlayer){

        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        // freeze
        if (moveInfo.freezeAction && moveInfo.effectSucces)
        {
            if (defendingFightPlayer.freeze == 0)
            {
                if (defendingOwnerPokemon.pokemon.type1 == "ice" || defendingOwnerPokemon.pokemon.type2 == "ice")
                {
                    fight.roundResult.battleActions.add(new MessageAction(defendingOwnerPokemon.pokemon.name + " is immune to freeze."))

                }
                else
                {
                    Recover.removeAllStatusAfflictions(defendingFightPlayer);
                    defendingFightPlayer.freeze = 1;
                    fight.roundResult.battleActions.add(new MessageAction(defendingOwnerPokemon.pokemon.name + " is frozen."))

                    moveInfo.freezeActionSucces = true
                }
            }
            else
            {
                if (moveInfo.attackMoveeffectProb == 0 || moveInfo.attackMoveeffectProb == 100)
                    fight.roundResult.battleActions.add(new MessageAction(defendingOwnerPokemon.pokemon.name + " is already frozen."))
            }
        }
    }

}
