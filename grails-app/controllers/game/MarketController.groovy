package game

import game.action.MarketAction
import game.item.Item
import map.View
import game.context.PlayerData
import grails.converters.JSON

class MarketController {

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        MarketAction marketAction = MarketAction.findByPositionXGreaterThanEqualsAndPositionXLessThanEqualsAndPositionYGreaterThanEqualsAndPositionYLessThanEquals(player.positionX-1,player.positionX+1, player.positionY-1,player.positionY+1)

        if (player.view != View.ShowMarket || !marketAction){
            render text : "Zit niet in een PokeMarket"
        }
        else {
            render text: g.render(template: 'market', model: [money:player.money,marketItems:marketAction.market.marketItems])
        }
    }
	
	def jsonIndex() {
		PlayerData playerData = session.playerData
		Player player = playerData.getPlayer()

		MarketAction marketAction = MarketAction.findByPositionXGreaterThanEqualsAndPositionXLessThanEqualsAndPositionYGreaterThanEqualsAndPositionYLessThanEquals(player.positionX-1,player.positionX+1, player.positionY-1,player.positionY+1)
		
		if (player.view != View.ShowMarket || !marketAction){
			throw new RuntimeException("Zit niet in een PokeMarket")
		}
		
		def model = [
			money:player.money,
			items: marketAction.market.marketItems.collect { MarketItem marketItem -> [
				name: marketItem.item.name,
				image: marketItem.item.image,
				id: marketItem.item.id,
				cost: marketItem.item.cost,
			]},
			
		]
		render model as JSON
	}

    def exit = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowMarket){
            render text : "Zit niet in een PokeMarket"
        }
        else {
            player.view = View.ShowMap
            player.save()
            redirect controller: 'game', action:'index'
        }
    }

    def buy = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        MarketAction marketAction = MarketAction.findByPositionXGreaterThanEqualsAndPositionXLessThanEqualsAndPositionYGreaterThanEqualsAndPositionYLessThanEquals(player.positionX-1,player.positionX+1, player.positionY-1,player.positionY+1)

        if (player.view != View.ShowMarket || !marketAction){
            render text : "Zit niet in een PokeMarket"
        }
        else {

            int itemId = Integer.parseInt(params.id)

            MarketItem marketItem = marketAction.market.marketItems.find{ MarketItem marketItem -> marketItem.item.id == itemId }
            Item item = marketItem?.item

            if (item){
                Items.addOwnerItem(player,item,true)
            }
        }

        redirect controller: 'game', action:'index'
    }

}
