package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 29-09-12
 * Time: 12:47
 * To change this template use File | Settings | File Templates.
 */
class BattleFunctions {

    Fight startFight(BattleType battleType, Owner player1, Pokemon wildPokemon, Integer wildPokemonLevel){
        Fight fight = new Fight()
        fight.battleType = battleType

        OwnerPokemon owner1Pokemon = getFirstAlivePokemon(player1)
        OwnerPokemon owner2Pokemon

        if (battleType == battleType.PVE) // player vs enemy
        {
            if (!wildPokemon){
                println("Geen wildPokemonId opgegeven");
                return null
            }
            if (!wildPokemonLevel){
                println("Geen wildPokemonLevel opgegeven");
                return null
            }

            owner2Pokemon = getWildPokemon(wildPokemon, wildPokemonLevel);
            owner2Pokemon.save()
        }
        else if (battleType == battleType.PVP){
            // :TODO implement
        }
        else if (battleType == battleType.PVN){
            // :TODO implement
        }

        FightPlayer fightPlayer1 = setBaseStats(fight,owner1Pokemon, PlayerType.user);




//        fight.fightPlayer2.save()
        // :TODO implement else
        fightPlayer1.save()
        fight.fightPlayer1 = fightPlayer1
        fightPlayer1.playerNr = 1
        fight.fightPlayer1.owner = player1




        FightPlayer fightPlayer2 = setBaseStats(fight,owner2Pokemon, PlayerType.wildPokemon);
        if (battleType == battleType.PVE){
            fightPlayer2.playerType = PlayerType.wildPokemon
        }
//
        fightPlayer2.playerNr = 2
        fightPlayer2.save()
        fight.fightPlayer2 = fightPlayer2

        fight.save(flush: true)


        // Weird have to set it again to set the right fightPlayer. For some reason player 2 is set
        fight.fightPlayer1 = fightPlayer1

        // koppel gevecht aan speler
        player1.fight = fight
        player1.save()



        return fight
    }

    public static double effectiveness(String attackType, String pokemonType1, String pokemonType2)
    {
        Effectiveness effectiveness = Effectiveness.findByType1AndType2AndAttackType(pokemonType1,pokemonType2,attackType)

        // Probeer type andersom voor resultaat
        if (pokemonType2 != "" && !effectiveness){
            effectiveness = Effectiveness.findByType1AndType2AndAttackType(pokemonType2,pokemonType1,attackType)
        }

        if (effectiveness){
            return effectiveness.effect
        }
        else {
            return 1.0
        }
    }

    /**
     * Neemt stats van pokemon over naar fight
     */
    FightPlayer setBaseStats(Fight fight, OwnerPokemon ownerPokemon, PlayerType playerType)
    {
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
                escapeAttempts : 0,
                holdMove : 0,
                holdTurns : 0,
                continueMove : null, //
                lastMove: null,
                playerType:playerType
        )
        return fightPlayer
    }

    // Bereken huidige stats behlve hp
    int calcStat(OwnerPokemon ownerPokemon, int ivStat, int baseStat)
    {
        int level = ownerPokemon.level

        int nature = 1; // Natuur van de pokemon (nog) niet gebruikt
        int ev = 0; // EV wordt (nog) niet gebruikt
        int stat = (((ivStat + 2 * baseStat + (ev/4)) * level/100) + 5) * nature;
        return Math.floor(stat);
    }


    OwnerPokemon getWildPokemon(Pokemon pokemon, int wildPokemonLevel){
        OwnerPokemon ownerPokemon = new OwnerPokemon(
                isNpc : false,
                pokemon: pokemon,
                hpIV:0,
                attackIV:0,
                defenseIV:0,
                spAttackIV:0,
                spDefenseIV:0,
                speedIV:0,
                hp:40,
                gender:Gender.Male,
                partyPosition: 0,
                xp:0,
                level:wildPokemonLevel
        )
        ownerPokemon.hp = ownerPokemon.calculateHP();
        return ownerPokemon
    }

    OwnerPokemon getFirstAlivePokemon(Owner owner){
        // :TODO Kan efficienter. Kan lijst met pokemon hebben
        List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwner(owner)
        return ownerPokemonList.sort{ !it.partyPosition }.last()
    }

}
