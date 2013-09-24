package game

import game.fight.log.MessageLog
import game.fight.status.Stats
import game.context.FightPlayer
import game.context.PlayerType
import game.context.BattleType
import game.context.Fight
import game.context.FightInvite
import game.context.FightPokemon
import map.MessageTranslator

class FightFactoryService {

    static transactional = false

    private static List<Fight> fights = new ArrayList<Fight>();

    static int fightCount = 0


    void endFight(Fight fight){
        fights.remove(fight)
//        FightData fightData = new FightData(
//                fightLog :fight.logHistory + fight.log
//        )   :TODO fix should be done on player exit or expiration
//        fightData.save()
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

            // Pre-battle message
            String message = ((Npc)player2).npcAction.message
            if (message){
                fight.roundResult.battleActions.add(new MessageLog(MessageTranslator.proces(message, player1)))
            }

            owner2Pokemon = Party.getFirstAlivePokemon(player2)
        }
        else if (battleType == battleType.PVP){
            owner2Pokemon = Party.getFirstAlivePokemon(player2)
        }

        assert owner1Pokemon
        assert owner2Pokemon

        PlayerType player2PlayerType
        if (battleType == battleType.PVE){
            player2PlayerType = PlayerType.wildPokemon
        }
        else if (battleType == battleType.PVN){
            player2PlayerType = PlayerType.npc
        }
        else if (battleType == battleType.PVP){
            player2PlayerType = PlayerType.user
        }

        FightPlayer fightPlayer2 = Stats.setBaseStats(fight,owner2Pokemon, player2PlayerType, 2);


        fight.fightPlayer2 = fightPlayer2

        FightPlayer fightPlayer1 = Stats.setBaseStats(fight,owner1Pokemon, PlayerType.user, 1);

        fight.fightPlayer1 = fightPlayer1
        fight.fightPlayer1.ownerId = player1.id


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
