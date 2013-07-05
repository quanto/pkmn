package game.admin

import game.Map
import game.MapLayout
import game.action.MapTransition
import game.action.Action
import game.action.RecoverAction
import game.action.ComputerAction
import game.action.MarketAction
import game.action.MapMessage
import game.action.NpcAction
import game.Owner
import game.Pokemon
import game.PokemonCreator
import game.Npc
import game.Market
import game.MarketItem
import game.Item
import game.OwnerItem
import game.action.PvpSelectAction
import game.action.BoulderAction
import game.action.BushAction
import game.action.FindItemAction
import game.RewardItem
import game.lock.OneTimeActionLock

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

        if (params.actionTypeClass == 'RecoverAction'){
            RecoverAction recoverAction = new RecoverAction(params)
            recoverAction.save()
        }
        else if (params.actionTypeClass == 'ComputerAction'){
            ComputerAction computerAction = new ComputerAction(params)
            computerAction.save()
        }
        else if (params.actionTypeClass == 'PvpSelectAction'){
            PvpSelectAction pvpSelectAction = new PvpSelectAction(params)
            pvpSelectAction.save()
        }
        else if (params.actionTypeClass == 'MapMessage'){
            MapMessage mapMessage = new MapMessage(params)
            mapMessage.save()
        }
        else if (params.actionTypeClass == 'MarketAction'){
            MarketAction marketAction = new MarketAction(params)
            marketAction.market = new Market()
            marketAction.market.save()
            marketAction.save()
        }
        else if (params.actionTypeClass == 'NpcAction'){
            NpcAction npcAction = new NpcAction(params)
            npcAction.owner = new Npc(npcAction:npcAction)
            npcAction.owner.name = params.name
            npcAction.owner.save()
            npcAction.save()
        }
        else if (params.actionTypeClass == 'BoulderAction'){
            BoulderAction action = new BoulderAction(params)
            action.save()
        }
        else if (params.actionTypeClass == 'BushAction'){
            BushAction action = new BushAction(params)
            action.save()
        }
        else if (params.actionTypeClass == 'FindItemAction'){
            FindItemAction action = new FindItemAction(params)
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
