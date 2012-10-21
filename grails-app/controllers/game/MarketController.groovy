package game

import map.View

class MarketController {

    def index() {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        MarketAction marketAction = MarketAction.findByPositionXAndPositionY(player.positionX, player.positionY)

        if (player.view != View.ShowMarket || !marketAction){
            render text : "Zit niet in een PokeMarket"
        }
        else {
            render text: g.render(template: 'market', model: [money:player.money,ownerItems:player.ownerItems,marketItems:marketAction.market.marketItems])
        }
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

}
