package game

import game.context.PlayerData
import game.item.Badge
import game.item.KeyItem
import game.item.UsableItem
import grails.converters.JSON
import grails.plugin.springsecurity.SpringSecurityUtils

class BagController {

    def index() { }

    def getItems(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        def list = [
                 usableItems: player.ownerItems.findAll{ it.item in UsableItem && showItem(it) }.collect{ OwnerItem ownerItem ->[
                        name: ownerItem.item.name,
                        image: ownerItem.item.image,
                         quantity: ownerItem.quantity
                 ]},
                 keyItems: player.ownerItems.findAll{ it.item in KeyItem && showItem(it) }.collect{ OwnerItem ownerItem ->[
                         name: ownerItem.item.name,
                         image: ownerItem.item.image
                 ]},
                 badges: player.ownerItems.findAll{ it.item in Badge && showItem(it) }.collect{ OwnerItem ownerItem ->[
                         name: ownerItem.item.name,
                         image: ownerItem.item.image
                 ]}
        ]
        render list as JSON
    }

    private showItem(OwnerItem ownerItem){
        return !ownerItem.item.hidden || SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')
    }
}
