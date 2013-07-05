package game

import game.action.MarketAction
import map.View
import game.context.PlayerData

class MarketController {

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        MarketAction marketAction = MarketAction.findByPositionXAndPositionY(player.positionX, player.positionY)

        if (player.view != View.ShowMarket || !marketAction){
            render text : "Zit niet in een PokeMarket"
        }
        else {
            render text: g.render(template: 'market', model: [money:player.money,marketItems:marketAction.market.marketItems])
        }
    }

    def exit = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        MarketAction marketAction = MarketAction.findByPositionXAndPositionY(player.positionX, player.positionY)

        if (player.view != View.ShowMarket || !marketAction){
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

        MarketAction marketAction = MarketAction.findByPositionXAndPositionY(player.positionX, player.positionY)

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
