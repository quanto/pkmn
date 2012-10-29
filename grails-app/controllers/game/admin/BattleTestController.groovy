package game.admin

import map.View
import game.FightFactoryService
import game.context.PlayerData
import game.Player
import game.Pokemon
import game.context.Fight
import game.context.BattleType

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


    def moveOverview(){

    }

}
