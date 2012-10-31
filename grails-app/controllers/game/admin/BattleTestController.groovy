package game.admin

import map.View
import game.FightFactoryService
import game.context.PlayerData
import game.Player
import game.Pokemon
import game.context.Fight
import game.context.BattleType
import game.PokemonCreator
import game.OwnerPokemon
import game.fight.status.Recover

class BattleTestController {

    FightFactoryService fightFactoryService

    def index() {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (request.post){
            Pokemon pokemon = Pokemon.get(params.pokemon)
            int level = Integer.parseInt(params.level)

            Fight fight = fightFactoryService.startFight(BattleType.PVE,player,null,pokemon,level)

            player.fightNr = fight.nr
            player.view = View.Battle
            redirect controller : 'game'
        }

        render view : "index"
    }

    def recover(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Recover.recoverParty(player)

        redirect action : 'index'
    }

    def setPkmn(){
        Pokemon pokemon = Pokemon.get(params.pokemon)
        int level = Integer.parseInt(params.level)

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        // Remove the pkmn at the first position
        OwnerPokemon.findByOwnerAndPartyPosition(player,1)?.delete(flush: true)
        // Add the pkmn to the new pos
        PokemonCreator.addOwnerPokemonToOwner(pokemon, level, player)

        redirect action : 'index'
    }


    def moveOverview(){

    }

}
