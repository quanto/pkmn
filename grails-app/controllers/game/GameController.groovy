package game

import grails.converters.JSON
import map.View
import game.context.PlayerData

class GameController {

    FightFactoryService fightFactoryService
    MapService mapService
    MarketService marketService
    PartyService partyService
    BattleService battleService
    PvpSelectService pvpSelectService

    def index() {
        // Don't remove
    }

    def action(){

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view == View.ShowMap){

            String direction = params.direction
            player.positionX = Integer.parseInt(params.x)
            player.positionY = Integer.parseInt(params.y)

            flash.direction = direction

            render mapService.getActionModel(player, direction) as JSON

        }
        else {
            render text: ""
        }
    }

    def checkBattle(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view == View.Battle){
            redirect action : "view"
        }
        else {
            render text: ""
        }
    }

    def checkMove(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view == View.ShowMap){

            String direction = params.direction
            player.positionX = Integer.parseInt(params.x)
            player.positionY = Integer.parseInt(params.y)

            flash.direction = direction

            render mapService.getMoveModel(player) as JSON

        }
        else {
            render text: ""
        }
    }
	
	def jsonView(){
		PlayerData playerData = session.playerData
		Player player = playerData.getPlayer()

        def model = [
                view: player.view.toString()
        ]

        if (player.view == View.ShowMap){
            model = model + mapService.getMapModel(player, flash.direction)
        }
        else if (player.view == View.ShowComputer){
            model = model + partyService.getComputerModel(player)
        }
        else if (player.view == View.ChoosePokemon){
            model = model
        }
        else if (player.view == View.ShowPvpSelect){
            model = model + pvpSelectService.getPvpSelectModel(player)
        }
        else if (player.view == View.Battle){
            model = model + battleService.getBattleModel(player)
        }
        else if (player.view == View.ShowMarket){
            model = model + marketService.getModel(player)
        }

		render model as JSON
	}



}
