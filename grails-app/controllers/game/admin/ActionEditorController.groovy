package game.admin

import game.Map
import game.MapLayout
import game.MapTransition
import game.Action
import game.RecoverAction
import game.ComputerAction
import game.MarketAction
import game.MapMessage
import game.NpcAction
import game.Owner
import game.Pokemon
import game.PokemonCreator
import game.Npc
import game.Market
import game.MarketItem
import game.Item
import game.OwnerItem
import game.PvpSelectAction
import game.BoulderAction
import game.BushAction

class ActionEditorController {

    def index() {

    }

    def actions(){
        Map map

        MapLayout mapLayout

        if (params.id){
            map = Map.get(params.id)

            if (map){
                mapLayout = MapLayout.createMapArray(map)
            }
        }
        render view: "actions", model: [map: map, mapLayout:mapLayout]
    }

    def choseMapTransition(){
        Map map

        MapLayout mapLayout

        if (params.id){
            map = Map.get(params.id)

            if (map){
                mapLayout = MapLayout.createMapArray(map)
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


    def getAction(){
        Map mapFrom = Map.get(params.id)
        int positionX = Integer.parseInt(params.positionX)
        int positionY = Integer.parseInt(params.positionY)

        Action action = Action.findByMapAndPositionXAndPositionY(mapFrom,positionX,positionY)

        if (action){
            render text: g.render(template: 'actionInfo', model: [action:action])
        }
        else {
            render text : g.render(template: 'createAction', model: [positionX:positionX,positionY:positionY,map:mapFrom])
        }
    }

    def createAction(){

        if (params.actionType == 'RecoverAction'){
            RecoverAction recoverAction = new RecoverAction(params)
            recoverAction.save()
        }
        else if (params.actionType == 'ComputerAction'){
            ComputerAction computerAction = new ComputerAction(params)
            computerAction.save()
        }
        else if (params.actionType == 'PvpSelectAction'){
            PvpSelectAction pvpSelectAction = new PvpSelectAction(params)
            pvpSelectAction.save()
        }
        else if (params.actionType == 'MapMessage'){
            MapMessage mapMessage = new MapMessage(params)
            mapMessage.save()
        }
        else if (params.actionType == 'MarketAction'){
            MarketAction marketAction = new MarketAction(params)
            marketAction.market = new Market()
            marketAction.market.save()
            marketAction.save()
        }
        else if (params.actionType == 'NpcAction'){
            NpcAction npcAction = new NpcAction(params)
            npcAction.owner = new Npc(npcAction:npcAction)
            npcAction.owner.name = params.name
            npcAction.owner.save()
            npcAction.save()
        }
        else if (params.actionType == 'BoulderAction'){
            BoulderAction action = new BoulderAction(params)
            action.save()
        }
        else if (params.actionType == 'BushAction'){
            BushAction action = new BushAction(params)
            action.save()
        }

        redirect action:'actions', id: params.map.id
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
        render text: "done"
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

    def removeMarketItem(){
        MarketItem marketItem = MarketItem.get(Integer.parseInt(params.id))
        Market market = marketItem.market
        market.removeFromMarketItems(marketItem)
        marketItem.delete()
        render text: "done"
    }

}
