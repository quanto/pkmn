package game

class FightFactoryService {

    static transactional = false

    private static List<Fight> fights = new ArrayList<Fight>();
    int fightCount = 0


    void endFight(Fight fight){
        fights.remove(fight)
        FightData fightData = new FightData(
                fightLog :fight.logHistory + fight.log
        )
        fightData.save()
    }

    public Fight getFight(int nr){
        return fights.find{ it.nr == nr }
    }

    Fight startFight(BattleType battleType, Player player1, MapPokemon mapPokemon){
        Random random = new Random()

        // Take a random level
        int level = mapPokemon.fromLevel
        if(mapPokemon.toLevel - mapPokemon.fromLevel > 0){
            level = mapPokemon.fromLevel + random.nextInt(mapPokemon.toLevel - mapPokemon.fromLevel)
        }

        return startFight(battleType, player1, mapPokemon.pokemon, level)
    }

    Fight startFight(BattleType battleType, Player player1, Pokemon wildPokemon, Integer wildPokemonLevel){
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

        FightPlayer fightPlayer1 = Stats.setBaseStats(fight,owner1Pokemon, PlayerType.user);

//        fight.fightPlayer2.save()
        // :TODO implement else
        fight.fightPlayer1 = fightPlayer1
        fightPlayer1.playerNr = 1
        fight.fightPlayer1.owner = player1




        FightPlayer fightPlayer2 = Stats.setBaseStats(fight,owner2Pokemon, PlayerType.wildPokemon);
        if (battleType == battleType.PVE){
            fightPlayer2.playerType = PlayerType.wildPokemon
        }

        fightPlayer2.playerNr = 2
        fight.fightPlayer2 = fightPlayer2


        // Weird have to set it again to set the right fightPlayer. For some reason player 2 is set
        fight.fightPlayer1 = fightPlayer1

        fight.nr = fightCount++

        fights.add(fight)


        //player1.save()

        return fight
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
