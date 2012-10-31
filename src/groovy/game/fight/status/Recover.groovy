package game.fight.status

import game.fight.log.MessageLog

import game.fight.log.MoveLog
import game.context.Fight
import game.context.FightPlayer
import game.Owner
import game.OwnerPokemon
import game.OwnerMove
import game.fight.Hp

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 19:43
 * To change this template use File | Settings | File Templates.
 */
class Recover {

    public static void recover(Fight fight, int recover, FightPlayer attackFightPlayer)
    {
        Hp.doRecover(attackFightPlayer,recover)

        // turn negative
        healthSlideLogAction(fight, attackFightPlayer,recover * -1);
        // log
        fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " recovers ${recover} hp."))

    }

    public static void healthSlideLogAction(Fight fight, FightPlayer fightPlayer, int damage)
    {

        if (fightPlayer.hp - damage < 0)
        {
            fightPlayer.hp = 0
        }
        /*
          else if (fightPlayer.Hp"} - $damage > fightPlayer.MaxHp"})
              $damage = fightPlayer.Hp"} - fightPlayer.MaxHp"};
          */
        fight.roundResult.battleActions.add(new MoveLog(fightPlayer.hp, fightPlayer.playerNr))

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
            ownerPokemon.attackStage = 0
            ownerPokemon.defenseStage = 0
            ownerPokemon.spDefenseStage = 0
            ownerPokemon.spAttackStage = 0
            ownerPokemon.speedStage = 0
            ownerPokemon.accuracyStage = 0
            ownerPokemon.criticalStage = 0
            ownerPokemon.evasionStage = 0
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
        fightPlayer.burn = 0;
        fightPlayer.freeze = 0;
        fightPlayer.paralysis = 0;
        fightPlayer.poison = 0;
        fightPlayer.badlypoisond = 0;
        fightPlayer.sleep = 0;
        fightPlayer.confusion = 0;
        fightPlayer.curse = 0;
        // :TODO uknown
        // fightPlayer.attract = 0;
    }
    
}
