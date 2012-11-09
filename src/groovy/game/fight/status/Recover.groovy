package game.fight.status

import game.fight.log.MessageLog

import game.fight.log.MoveLog
import game.context.Fight
import game.context.FightPlayer
import game.Owner
import game.OwnerPokemon
import game.OwnerMove
import game.fight.Hp

class Recover {

    public static void recover(Fight fight, int recover, FightPlayer attackFightPlayer)
    {
        Hp.doRecover(attackFightPlayer,recover)

        // turn negative
        healthSlideLogAction(fight, attackFightPlayer,recover * -1);
        // log
        fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.fightPokemon.name + " recovers ${recover} hp."))

    }

    public static void healthSlideLogAction(Fight fight, FightPlayer fightPlayer, int damage)
    {

        if (fightPlayer.fightPokemon.hp - damage < 0)
        {
            fightPlayer.fightPokemon.hp = 0
        }

        fight.roundResult.battleActions.add(new MoveLog(fightPlayer.fightPokemon.hp, fightPlayer.playerNr))

    }

    /**
     * Recover the hp en pp of party
     */
    public static void recoverParty(Owner owner)
    {
        List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwner(owner)

        ownerPokemonList.each { OwnerPokemon ownerPokemon ->
            ownerPokemon.hp = ownerPokemon.calculateHP()
            ownerPokemon.burn = 0
            ownerPokemon.freeze = 0
            ownerPokemon.paralysis = 0
            ownerPokemon.poison = 0
            ownerPokemon.badlyPoisond = 0
            ownerPokemon.sleep = 0
            ownerPokemon.confusion = 0
            ownerPokemon.curse = 0

            ownerPokemon.save()

            /**
             * Reset moves
             */
            ownerPokemon.ownerMoves.each { OwnerMove ownerMove ->
                ownerMove.ppLeft = ownerMove.move.pp
                ownerMove.save()
            }
        }
    }

    public static void removeAllStatusAfflictions(FightPlayer fightPlayer)
    {
        fightPlayer.fightPokemon.burn = 0
        fightPlayer.fightPokemon.freeze = 0
        fightPlayer.fightPokemon.paralysis = 0
        fightPlayer.fightPokemon.poison = 0
        fightPlayer.fightPokemon.badlypoisond = 0
        fightPlayer.fightPokemon.sleep = 0
        fightPlayer.fightPokemon.confusion = 0
        fightPlayer.fightPokemon.curse = 0
    }
    
}
