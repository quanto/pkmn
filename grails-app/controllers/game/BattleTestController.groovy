package game

import map.View

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
