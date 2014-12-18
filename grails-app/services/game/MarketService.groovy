package game

import game.action.MarketAction

class MarketService {

    static transactional = false

    public def getModel(Player player){
        MarketAction marketAction = MarketAction.findByPositionXGreaterThanEqualsAndPositionXLessThanEqualsAndPositionYGreaterThanEqualsAndPositionYLessThanEquals(player.positionX-1,player.positionX+1, player.positionY-1,player.positionY+1)

        if (!marketAction){
            assert false
        }

        def marketModel = [
                money:player.money,
                items: marketAction.market.marketItems.collect { MarketItem marketItem -> [
                        name: marketItem.item.name,
                        image: marketItem.item.image,
                        id: marketItem.item.id,
                        cost: marketItem.item.cost,
                ]},
        ]
        return marketModel
    }

}
