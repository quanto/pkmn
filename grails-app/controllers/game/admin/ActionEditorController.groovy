package game.admin

import game.AltMap
import game.Map
import game.MapLayout
import game.action.*
import game.Owner
import game.Pokemon
import game.PokemonCreator
import game.Npc
import game.Market
import game.MarketItem
import game.item.Item
import game.OwnerItem
import game.RewardItem
import game.lock.OneTimeActionLock

class ActionEditorController {

    def index() {

    }

    def actions(long id, long altMapId){
        Map map

        MapLayout mapLayout
        AltMap altMap

        def actions

        if (id){
            map = Map.get(id)

            if (map){
                altMap = params.altMap?:AltMap.get(altMapId)

                actions = altMap?.newActions?altMap.actions:map.actions

                mapLayout = MapLayout.createMapArray(map, altMap)
            }
        }
        render view: "actions", model: [map: map, mapLayout:mapLayout, actions: actions, altMap: altMap]
    }

    def choseMapTransition(){
        Map map

        MapLayout mapLayout

        if (params.id){
            map = Map.get(params.id)

            if (map){
                mapLayout = MapLayout.createMapArray(map, null)
            }
        }
        render text: g.render(template: 'choseMapTransition', model: [mapLayout:mapLayout,map: map])
    }

    def saveMapTransition(){

        Map mapFrom = Map.get(params.fromMap)
        Map mapTo = Map.get(params.toMap)

        MapTransition mapTransition1 = new MapTransition(
                positionX : params.fromX,
                positionY : params.fromY,
                map : mapFrom
        )
        mapTransition1.save()
        MapTransition mapTransition2 = new MapTransition(
                positionX : params.toX,
                positionY : params.toY,
                map : mapTo,
                jumpTo: mapTransition1

        )
        mapTransition1.jumpTo = mapTransition2

        mapFrom.addToActions(mapTransition1)
        mapTo.addToActions(mapTransition2)

        render text: ""
    }


    def getAction(long id, long altMapId){
        Map mapFrom = Map.get(id)
        AltMap altMap = AltMap.get(altMapId)
        int positionX = Integer.parseInt(params.positionX)
        int positionY = Integer.parseInt(params.positionY)

        Action action = altMap?Action.findByAltMapAndPositionXAndPositionY(altMap,positionX,positionY):Action.findByMapAndPositionXAndPositionY(mapFrom,positionX,positionY)

        if (action){
            render text: g.render(template: 'actionInfo', model: [action:action])
        }
        else {
            render text : g.render(template: 'createAction', model: [positionX: positionX, positionY: positionY, map: mapFrom, altMap: altMap])
        }
    }

    def createAction(Long altMapId){
        Action action
        if (params.actionTypeClass == 'RecoverAction'){
            action = new RecoverAction(params)
        }
        else if (params.actionTypeClass == 'ComputerAction'){
            action = new ComputerAction(params)
        }
        else if (params.actionTypeClass == 'PvpSelectAction'){
            action = new PvpSelectAction(params)
        }
        else if (params.actionTypeClass == 'MapMessage'){
            action = new MapMessage(params)
        }
        else if (params.actionTypeClass == 'MarketAction'){
            action = new MarketAction(params)
            action.market = new Market()
            action.market.save()
        }
        else if (params.actionTypeClass == 'NpcAction'){
            action = new NpcAction(params)
            action.owner = new Npc(npcAction:npcAction)
            action.owner.name = params.name
            action.owner.save()
        }
        else if (params.actionTypeClass == 'BoulderAction'){
            action = new BoulderAction(params)
        }
        else if (params.actionTypeClass == 'BushAction'){
            action = new BushAction(params)
        }
        else if (params.actionTypeClass == 'FindItemAction'){
            action = new FindItemAction(params)
        }
        else if (params.actionTypeClass == 'PersonAction'){
            action = new PersonAction(params)
        }
        else if (params.actionTypeClass == 'MessagePersonAction'){
            action = new MessagePersonAction(params)
        }
        else if (params.actionTypeClass == 'PokemonAction'){
            action = new PokemonAction(params)
        }

        assert action
        assert action.map.id

        // If we got an altMap it should not belong to the map
        if (altMapId){
            println "altMap"
            action.map = null
            action.altMap = AltMap.get(altMapId)
        }

        action.save()

        redirect action:'actions', id: params.map.id, params: [altMapId: action.altMap?.id]
    }

    def addPokemonToNpc(){
        Owner owner = Npc.get(Integer.parseInt(params.owner))
        int pkmnId = Integer.parseInt(params.pokemon)
        Pokemon pokemon = Pokemon.get(pkmnId)
        PokemonCreator.addOwnerPokemonToOwner(pokemon, Integer.parseInt(params.level), owner)
        render text: "done"
    }

    def addItem(){

        Market market = Market.get(Integer.parseInt(params.market))
        MarketItem marketItem = new MarketItem(
                market: market,
                item : Item.get(Integer.parseInt(params.item))
        )
        market.addToMarketItems(marketItem)
        render text: "done"
    }

    def updateAction(){
        int actionId = Integer.parseInt(params.actionId)
        Action action = Action.get(actionId)
        action.properties = params

        flash.message = "Action ${actionId} aangepast"

        redirect action:'actions', id: action.map?.id?:action.altMap?.map?.id, params: [altMapId: action.altMap?.id]
    }

    def deleteAction(){
        Action action = Action.get(Integer.parseInt(params.id))

        // Remove childs
        if (action in NpcAction){
            action.owner.delete()
        }
        else if (action in MarketAction){
            action.market.delete()
        }
        else if (action in MapTransition){
            action.jumpTo.delete()
        }

        OneTimeActionLock.findAllByAction(action).each { it.delete() }

        action.delete()

        render text: "done"
    }

    def addRewardItemToNpc(){
        Npc npc = Npc.get(Integer.parseInt(params.owner))
        OwnerItem ownerItem = new OwnerItem(
                quantity: Integer.parseInt(params.quantity),
                item:Item.get(Integer.parseInt(params.item))
        )
        ownerItem.owner = npc
        npc.addToRewardItems(ownerItem)
        render text: "done"
    }

    def addRewardItem(){
        FindItemAction action = FindItemAction.get(Integer.parseInt(params.actionId))
        RewardItem rewardItem = new RewardItem(
                quantity: Integer.parseInt(params.quantity),
                item:Item.get(Integer.parseInt(params.item))
        )
        action.addToRewardItems(rewardItem)
        render text: "done"
    }

    def removeMarketItem(){
        MarketItem marketItem = MarketItem.get(Integer.parseInt(params.id))
        Market market = marketItem.market
        market.removeFromMarketItems(marketItem)
        marketItem.delete()
        render text: "done"
    }

}
