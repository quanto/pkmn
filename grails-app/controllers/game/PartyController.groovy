package game

import map.View

class PartyController {

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        boolean computerView = false
        def ownerPokemonList = OwnerPokemon.findAllByPartyPositionGreaterThanAndOwner(0,player)

        render text: g.render(template: 'party', model: [computerView:computerView,ownerPokemonList:ownerPokemonList])
    }

    def moveUp = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        OwnerPokemon ownerPokemon = OwnerPokemon.get(params.id)

        // error checking
        if (!ownerPokemon || ownerPokemon.owner != player)
        {
            render text:"Pokemon niet van eigenaar!"
        }
        else if (ownerPokemon.partyPosition == 1)
        {
            render text:"Pokemon kan niet verplaatst worden"
        }
        else if (ownerPokemon.partyPosition == 0)
        {
            render text:"Pokemon is niet in party"
        }
        else {
            OwnerPokemon switchOwnerPokemon = OwnerPokemon.findByOwnerAndPartyPosition(player,ownerPokemon.partyPosition-1)

            if (!switchOwnerPokemon){
                println "2"
                render text:"Pokemon kan niet verplaatst worden"
            }
            else {
                ownerPokemon.partyPosition -= 1
                switchOwnerPokemon.partyPosition += 1
                redirect controller:'game',action:"index"
            }
        }

    }

    def moveDown = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        OwnerPokemon ownerPokemon = OwnerPokemon.get(params.id)

        // error checking
        if (!ownerPokemon || ownerPokemon.owner != player)
        {
            render text:"Pokemon niet van eigenaar!"
        }
        else if (ownerPokemon.partyPosition == 6)
        {
            render text:"Pokemon kan niet verplaatst worden"
        }
        else if (ownerPokemon.partyPosition == 0)
        {
            render text:"Pokemon is niet in party"
        }
        else {
            OwnerPokemon switchOwnerPokemon = OwnerPokemon.findByOwnerAndPartyPosition(player,ownerPokemon.partyPosition+1)

            if (!switchOwnerPokemon){
                println "2"
                render text:"Pokemon kan niet verplaatst worden"
            }
            else {
                ownerPokemon.partyPosition += 1
                switchOwnerPokemon.partyPosition -= 1
                redirect controller:'game',action:"index"
            }
        }
    }

    def computer = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowComputer){
            render text : "Zit niet bij de computer"
        }
        else {

            boolean computerView = true

            def ownerPokemonList = OwnerPokemon.findAllByOwner(player)
            def partyList = ownerPokemonList?.findAll() { it.partyPosition > 0 }
            def computerList = ownerPokemonList?.findAll() { it.partyPosition == 0 }

            render text: g.render(template: 'computer', model: [computerView:computerView,partyList:partyList,computerList:computerList])
        }
    }

}