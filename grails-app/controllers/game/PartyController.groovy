package game

import grails.converters.JSON
import map.View
import game.context.PlayerData

class PartyController {

    PartyService partyService

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        render partyService.getPartyModel(player) as JSON
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
                render text:"Pokemon kan niet verplaatst worden"
            }
            else {
                ownerPokemon.partyPosition -= 1
                switchOwnerPokemon.partyPosition += 1
                ownerPokemon.save()
                switchOwnerPokemon.save(flush:true)
                render text: ""
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
                render text:"Pokemon kan niet verplaatst worden"
            }
            else {
                ownerPokemon.partyPosition += 1
                switchOwnerPokemon.partyPosition -= 1
                ownerPokemon.save()
                switchOwnerPokemon.save(flush:true)
                redirect text: ""
            }
        }
    }

    def withdraw = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowComputer){
            render text : "Zit niet bij de computer"
        }
        else {
            OwnerPokemon ownerPokemon = OwnerPokemon.get(params.id)

            if (!ownerPokemon || ownerPokemon.owner != player)
            {
                render text:"Pokemon niet van eigenaar!"
            }
            else if (ownerPokemon.partyPosition != 0)
            {
                render text:"Pokemon al in team!"
            }

            int openPartyPosition = Party.getOpenPartyPosition(player)

            if (openPartyPosition){
                ownerPokemon.partyPosition = openPartyPosition
                ownerPokemon.save()
            }
            else {
                render text:"Kan pokemon niet toevoegen!"
            }
        }

        render text: ""
    }

    def release = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowComputer){
            render text : "Zit niet bij de computer"
        }
        else {
            OwnerPokemon ownerPokemon = OwnerPokemon.get(params.id)

            if (!ownerPokemon || ownerPokemon.owner != player)
            {
                render text:"Pokemon niet van eigenaar!"
            }
            else if (ownerPokemon.partyPosition != 0)
            {
                render text:"Pokemon zit in team!"
            }
            else {
                ownerPokemon.delete()
            }
        }
        redirect controller: 'game', action:'index'
    }

    def deposit = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowComputer){
            render text : "Zit niet bij de computer"
        }
        else {
            OwnerPokemon ownerPokemon = OwnerPokemon.get(params.id)

            if (!ownerPokemon || ownerPokemon.owner != player)
            {
                render text:"Pokemon niet van eigenaar!"
            }
//            else if (OwnerPokemon.countByPartyPositionGreaterThanAndHpGreaterThan(0,0) == 0)
//            {
//                // TODO bovenstaande klopt niet
//                render text:"Pokemon kan niet uit het team worden gezet. Er moet minstens 1 levende pokemon aanwezig zijn"
//            }

            ownerPokemon.partyPosition = 0
            ownerPokemon.save(flush: true)
            partyService.correctPartyPositions(player)

            render text: ""
        }
    }

    def exit = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowComputer){
            render text : "Zit niet bij de computer"
        }
        else {
            player.view = View.ShowMap
            player.save(flush: true)
            render text: ""
        }
    }

    def pokemon(){
        Pokemon pokemon = Pokemon.get(Integer.parseInt(params.id))
        render text: g.render(template: 'pokemon', model: [pokemon:pokemon])
    }

}
