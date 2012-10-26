package game

class FightFactoryService {

    static transactional = false

    private static List<Fight> fights = new ArrayList<Fight>();
    int fightCount = 0


    void endFight(Fight fight){
        fights.remove(fight)
//        FightData fightData = new FightData(
//                fightLog :fight.logHistory + fight.log
//        )   :TODO fix
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

        return startFight(battleType, player1, null, mapPokemon.pokemon, level)
    }

    Fight startFight(BattleType battleType, Owner player1, Owner player2, Pokemon wildPokemon, Integer wildPokemonLevel){
        Fight fight = new Fight()
        fight.battleType = battleType

        assert player1

        OwnerPokemon owner1Pokemon = Party.getFirstAlivePokemon(player1)
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
            owner2Pokemon = Party.getFirstAlivePokemon(player2)
        }
        else if (battleType == battleType.PVN){
            owner2Pokemon = Party.getFirstAlivePokemon(player2)
        }
        else if (battleType == battleType.PVP){
            owner2Pokemon = Party.getFirstAlivePokemon(player2)
        }

        assert owner1Pokemon
        assert owner2Pokemon

        FightPlayer fightPlayer1 = Stats.setBaseStats(fight,owner1Pokemon, PlayerType.user, 1);

        fight.fightPlayer1 = fightPlayer1
        fight.fightPlayer1.owner = player1

        FightPlayer fightPlayer2 = Stats.setBaseStats(fight,owner2Pokemon, PlayerType.wildPokemon, 2);

        if (battleType == battleType.PVE){
            fightPlayer2.playerType = PlayerType.wildPokemon
        }
        else if (battleType == battleType.PVP){
            fightPlayer2.playerType = PlayerType.user
        }

        fight.fightPlayer2 = fightPlayer2


        // Weird have to set it again to set the right fightPlayer. For some reason player 2 is set
        fight.fightPlayer1 = fightPlayer1

        fight.nr = fightCount++

        fights.add(fight)

        return fight
    }

    OwnerPokemon getWildPokemon(Pokemon pokemon, int wildPokemonLevel){
        return PokemonCreator.getOwnerPokemon(pokemon,wildPokemonLevel)
    }

}
