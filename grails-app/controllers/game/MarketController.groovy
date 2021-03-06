package game

import game.action.MarketAction
import game.item.Item
import map.View
import game.context.PlayerData

class MarketController {

    def exit = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowMarket){
            render text : "Zit niet in een PokeMarket"
        }
        else {
            player.view = View.ShowMap
            player.save()
            render text: ""
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

        render text: ''
    }

}
