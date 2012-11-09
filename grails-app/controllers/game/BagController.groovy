package game

import game.context.PlayerData

class BagController {

    def index() { }

    def getItems(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (params.itemTab == "usableItems"){

            render text: g.render(template: 'ownerItems', model: [ownerItems:player.ownerItems.findAll{ it.item in UsableItem }])
        }
        else if (params.itemTab == "keyItems"){
            render text: g.render(template: 'ownerItems', model: [ownerItems:player.ownerItems.findAll{ it.item in KeyItem }])
        }
        else {
            render text: g.render(template: 'badges', model: [ownerItems:player.ownerItems.findAll{ it.item in Badge }])
        }
    }
}
