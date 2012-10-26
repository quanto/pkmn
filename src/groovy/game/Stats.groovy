package game

import game.fight.MessageAction
import game.fight.SwitchAction

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 20:09
 * To change this template use File | Settings | File Templates.
 */
class Stats {

    /**
     * Neemt stats van pokemon over naar fight
     */
    public static setBaseStats(Fight fight, OwnerPokemon ownerPokemon, PlayerType playerType, int playerNr)
    {

        if (ownerPokemon.owner){
            fight.roundResult.battleActions.add(new MessageAction("${ownerPokemon.owner.name} brings out ${ownerPokemon.pokemon.name}"))
        }
        else {
            fight.roundResult.battleActions.add(new MessageAction("A wild ${ownerPokemon.pokemon.name} appears"))
        }

        // We should only track the used pokemon for users
        if (playerType == PlayerType.user && fight.battleType != BattleType.PVP){
            // Remove fainted pokemon
            if (fight.fightPlayer1?.ownerPokemon && fight.fightPlayer1.ownerPokemon.hp <= 0){
                OwnerPokemon currentOwnerPokemon = fight.usedPokemon.find{ it.id == fight.fightPlayer1.ownerPokemon.id}
                fight.usedPokemon.remove(currentOwnerPokemon)
            }
            // Add the added pokemon
            if (!fight.usedPokemon.find{ it.id == ownerPokemon.id}){
                fight.usedPokemon.add(ownerPokemon)
            }
        }

        FightPlayer fightPlayer = new FightPlayer(
                fight: fight,
                ownerPokemon:ownerPokemon,
                owner: ownerPokemon.owner,
                attack:calcStat(ownerPokemon,ownerPokemon.attackIV,ownerPokemon.pokemon.baseAttack),
                defense: calcStat(ownerPokemon,ownerPokemon.defenseIV,ownerPokemon.pokemon.baseDefense),
                spAttack: calcStat(ownerPokemon,ownerPokemon.spAttackIV,ownerPokemon.pokemon.baseSpAttack),
                spDefense : calcStat(ownerPokemon,ownerPokemon.spDefenseIV,ownerPokemon.pokemon.baseSpDefense),
                speed : calcStat(ownerPokemon,ownerPokemon.speedIV,ownerPokemon.pokemon.baseSpeed),
                maxHp : ownerPokemon.calculateHP(),
                hp : ownerPokemon.hp,
                level : ownerPokemon.level,
                burn : ownerPokemon.burn,
                freeze : ownerPokemon.freeze,
                paralysis : ownerPokemon.paralysis,
                poison : ownerPokemon.poison,
                badlypoisond : ownerPokemon.badlyPoisond,
                sleep : ownerPokemon.sleep,
                confusion : ownerPokemon.confusion,
                curse :ownerPokemon.curse,
                attackStage :ownerPokemon.attackStage,
                defenseStage : ownerPokemon.defenseStage,
                spAttackStage : ownerPokemon.spAttackStage,
                spDefenseStage : ownerPokemon.spDefenseStage,
                speedStage : ownerPokemon.speedStage,
                accuracyStage : ownerPokemon.accuracyStage,
                criticalStage : ownerPokemon.criticalStage,
                evasionStage : ownerPokemon.evasionStage,
                //   move
                //   learnMoves
                holdMove : 0,
                holdTurns : 0,
                continueMove : null, //
                lastMove: null,
                playerType:playerType,
                playerNr: playerNr
        )

        fight.roundResult.battleActions.add(new SwitchAction(ownerPokemon, playerNr))

        return fightPlayer
    }

    // Bereken huidige stats behlve hp
    public static int calcStat(OwnerPokemon ownerPokemon, int ivStat, int baseStat)
    {
        int level = ownerPokemon.level

        int nature = 1; // Natuur van de pokemon (nog) niet gebruikt
        int ev = 0; // EV wordt (nog) niet gebruikt
        int stat = (((ivStat + 2 * baseStat + (ev/4)) * level/100) + 5) * nature;
        return Math.floor(stat);
    }

    /**
     * Uitgevoerd bij het switchen van pokemon
     * En na het gevecht, na het gevecht stage weer op 0
     */
    public static void saveStats(FightPlayer fightPlayer, boolean endBattle = false)
    {
        OwnerPokemon ownerPokemon = fightPlayer.ownerPokemon

        ownerPokemon.hp = fightPlayer.hp
        ownerPokemon.level = fightPlayer.level
        ownerPokemon.burn = fightPlayer.burn
        ownerPokemon.freeze = fightPlayer.freeze
        ownerPokemon.paralysis = fightPlayer.paralysis
        ownerPokemon.badlyPoisond = fightPlayer.badlypoisond
        ownerPokemon.poison = fightPlayer.poison
        ownerPokemon.sleep = fightPlayer.sleep
        ownerPokemon.confusion = fightPlayer.confusion
        ownerPokemon.curse = fightPlayer.curse

        if (!endBattle)
        {
            ownerPokemon.attackStage = fightPlayer.attackStage
            ownerPokemon.defenseStage = fightPlayer.defenseStage
            ownerPokemon.spAttackStage = fightPlayer.spAttackStage
            ownerPokemon.spDefenseStage = fightPlayer.spDefenseStage
            ownerPokemon.speedStage = fightPlayer.speedStage
            ownerPokemon.accuracyStage = fightPlayer.accuracyStage
            ownerPokemon.criticalStage = fightPlayer.criticalStage
            ownerPokemon.evasionStage = fightPlayer.evasionStage
        }
        else
        {
            ownerPokemon.attackStage = 0
            ownerPokemon.defenseStage = 0
            ownerPokemon.spAttackStage = 0
            ownerPokemon.spDefenseStage = 0
            ownerPokemon.speedStage = 0
            ownerPokemon.accuracyStage = 0
            ownerPokemon.criticalStage = 0
            ownerPokemon.evasionStage = 0
        }
        ownerPokemon.save()
    }

}
