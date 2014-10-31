package game.fight.status

import game.fight.log.MessageLog

import game.context.Fight
import game.OwnerPokemon
import game.context.PlayerType
import game.context.BattleType
import game.context.FightPlayer
import game.context.FightPokemon
import game.Owner
import game.fight.log.InitialHpLog

class Stats {

    public static void bringOutPokemon(Fight fight, FightPlayer fightPlayer, FightPokemon fightPokemon){
        
		if (fightPlayer.playerType != PlayerType.wildPokemon){
            fight.roundResult.battleActions.add(new MessageLog("${fightPlayer.name} brings out ${fightPokemon.name}"))
        }
        else {
            fight.roundResult.battleActions.add(new MessageLog("A wild ${fightPokemon.name} appears"))
        }

        // We should only track the used pokemon for users
        if (fightPlayer.playerType == PlayerType.user && fight.battleType != BattleType.PVP){
            // Remove fainted pokemon
            if (fight.fightPlayer1?.fightPokemon && fight.fightPlayer1.fightPokemon.hp <= 0){
                fight.usedFightPokemon.remove(fight.fightPlayer1?.fightPokemon)
            }
            // Add the added pokemon
            if (!fight.usedFightPokemon.find{ it.ownerPokemonId == fightPokemon.ownerPokemonId}){
                fight.usedFightPokemon.add(fightPokemon)
            }
        }

        fightPlayer.fightPokemon = fightPokemon

        fight.roundResult.initialActions.add(new InitialHpLog(fightPokemon,fightPlayer.playerNr))

    }


    /**
     * Neemt stats van pokemon over naar fight
     */
    public static setBaseStats(Fight fight, OwnerPokemon ownerPokemon, PlayerType playerType, int playerNr)
    {
        FightPlayer fightPlayer = new FightPlayer(
                fight: fight,
                ownerId: ownerPokemon.owner?.id,
                name: ownerPokemon.owner?.name,
                //   move
                //   learnMoves
                holdMove : 0,
                holdTurns : 0,
                continueMove : null,
                lastBattleAction: null,
                playerType:playerType,
                playerNr: playerNr
        )

        if (playerType != PlayerType.wildPokemon){
            fightPlayer.party = createParty(ownerPokemon.owner)
            // Set the active pokemon
            bringOutPokemon(fight, fightPlayer, fightPlayer.party.find { it.ownerPokemonId == ownerPokemon.id })
        }
        else {
            fightPlayer.party = [createFightPokemon(ownerPokemon)]
            bringOutPokemon(fight, fightPlayer,fightPlayer.party.last())
        }

        // Set them back on the fight
        if (playerNr == 1){
            fight.fightPlayer1 = fightPlayer
        }
        else {
            fight.fightPlayer2 = fightPlayer
        }


        return fightPlayer
    }

    public static List<FightPokemon> createParty(Owner owner){
        def party = []
        OwnerPokemon.findAllByPartyPositionGreaterThanAndOwner(0,owner).each{ OwnerPokemon ownerPokemon ->
            party.add(createFightPokemon(ownerPokemon))
        }
        return party
    }

    public static FightPokemon createFightPokemon(OwnerPokemon ownerPokemon){
        return new FightPokemon(
                name: ownerPokemon.pokemon.name,
                hp: ownerPokemon.hp,
                ownerPokemonId: ownerPokemon.id,
                attack:calcStat(ownerPokemon,ownerPokemon.attackIV,ownerPokemon.pokemon.baseAttack),
                defense: calcStat(ownerPokemon,ownerPokemon.defenseIV,ownerPokemon.pokemon.baseDefense),
                spAttack: calcStat(ownerPokemon,ownerPokemon.spAttackIV,ownerPokemon.pokemon.baseSpAttack),
                spDefense : calcStat(ownerPokemon,ownerPokemon.spDefenseIV,ownerPokemon.pokemon.baseSpDefense),
                speed : calcStat(ownerPokemon,ownerPokemon.speedIV,ownerPokemon.pokemon.baseSpeed),
                maxHp : ownerPokemon.calculateHP(),
                level : ownerPokemon.level,
                burn : ownerPokemon.burn,
                freeze : ownerPokemon.freeze,
                paralysis : ownerPokemon.paralysis,
                poison : ownerPokemon.poison,
                badlypoisond : ownerPokemon.badlyPoisond,
                sleep : ownerPokemon.sleep,
                confusion : ownerPokemon.confusion,
                curse :ownerPokemon.curse,
                attackStage : 0,
                defenseStage : 0,
                spAttackStage : 0,
                spDefenseStage : 0,
                speedStage : 0,
                accuracyStage : 0,
                criticalStage : 0,
                evasionStage : 0,
                type1: ownerPokemon.pokemon.type1,
                type2: ownerPokemon.pokemon.type2,
                pokemonNr: ownerPokemon.pokemon.nr,
                gender: ownerPokemon.gender,
                partyPosition: ownerPokemon.partyPosition
        )
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
     * Save stats after the battle
     */
    public static void saveStats(FightPlayer fightPlayer, boolean endBattle = false)
    {
        fightPlayer.party.each { FightPokemon fightPokemon ->

            OwnerPokemon ownerPokemon = fightPokemon.ownerPokemon

            ownerPokemon.hp = fightPokemon.hp
            ownerPokemon.level = fightPokemon.level
            ownerPokemon.burn = fightPokemon.burn
            ownerPokemon.freeze = fightPokemon.freeze
            ownerPokemon.paralysis = fightPokemon.paralysis
            ownerPokemon.badlyPoisond = fightPokemon.badlypoisond
            ownerPokemon.poison = fightPokemon.poison
            ownerPokemon.sleep = fightPokemon.sleep
            ownerPokemon.confusion = fightPokemon.confusion
            ownerPokemon.curse = fightPokemon.curse

            ownerPokemon.save()
        }
    }

}
