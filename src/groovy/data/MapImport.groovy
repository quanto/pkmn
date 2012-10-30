package data

import game.Map
import game.MapMessage
import game.MapPokemon
import game.Pokemon
import game.ComputerAction
import game.RecoverAction
import game.MapTransition
import game.NpcAction
import game.Npc
import game.Market
import game.MarketAction
import game.PvpSelectAction

class MapImport {

    public static void importMaps(){
        println "Import map lines"

        Map.withTransaction {

            Map map
            new File('import/maps/').listFiles().each { File file ->

                String node = ""

                def parts = []

                file.eachLine { line ->

                    if (line.contains("</mapData>")){
                        node = ""
                        map = createMap(parts)
                        parts = []
                    }
                    else if (line.contains("</marketAction>")){
                        node = ""
                        importMarketAction(parts, map)
                        parts = []
                    }
                    else if (line.contains("</npcAction>")){
                        node = ""
                        importNpcAction(parts, map)
                        parts = []
                    }
                    else if (line.contains("</mapMessage>")){
                        node = ""
                        importMessage(parts, map)
                        parts = []
                    }
                    else if (line.contains("</recoverAction>")){
                        node = ""
                        importRecoverAction(parts,map)
                        parts = []
                    }
                    else if (line.contains("</pvpSelectAction>")){
                        node = ""
                        importPvpSelectAction(parts,map)
                        parts = []
                    }
                    else if (line.contains("</computerAction>")){
                        node = ""
                        importComputerAction(parts,map)
                        parts = []
                    }
                    else if (line.contains("</mapTransition>")){
                        node = ""
                        importMapTransition(parts,map)
                        parts = []
                    }
                    else if (line.contains("</pokemon>")){
                        node = ""
                        importMapPokemon(parts,map)
                        parts = []
                    }

                    if (node){
                        parts.add(line)
                    }

                    if (line.contains("<mapData>")){
                        node = "mapData"
                    }
                    else if (line.contains("<marketAction>")){
                        node = "marketAction"
                    }
                    else if (line.contains("<npcAction>")){
                        node = "npcAction"
                    }
                    else if (line.contains("<mapMessage>")){
                        node = "mapMessage"
                    }
                    else if (line.contains("<recoverAction>")){
                        node = "recoverAction"
                    }
                    else if (line.contains("<pvpSelectAction>")){
                        node = "pvpSelectAction"
                    }
                    else if (line.contains("<computerAction>")){
                        node = "computerAction"
                    }
                    else if (line.contains("<mapTransition>")){
                        node = "mapTransition"
                    }
                    else if (line.contains("<pokemon>")){
                        node = "pokemon"
                    }
                }

            }
        }

        // Loop again to couple map transitions (Can only be coupled after they have been created)
        new File('import/maps/').listFiles().each { File file ->

            def parts = []
            Map map
            String node
            file.eachLine { line ->

                if (line.contains("</mapData>")){
                    node = ""
                    map = Map.findByName(parts[0])
                    parts = []
                }
                else if (line.contains("</mapTransition>")){
                    node = ""
                    coupleMapTransitions(parts,map)
                    parts = []
                }

                if (node){
                    parts.add(line)
                }

                if (line.contains("<mapData>")){
                    node = "mapData"
                }
                else if (line.contains("<mapTransition>")){
                    node = "mapTransition"
                }

            }

        }

    }

    public static Map createMap(def parts){
        Map map = new Map(
                name : parts[0],
                dataBackground : parts[1],
                dataForeground : parts[2],
                active : parts[3] == '1',
                worldX: parts[4]=='null'?null:parts[4],
                worldY: parts[5]=='null'?null:parts[5]
        )
        map.save()
        return map
    }

    public static void importMarketAction(def parts, Map map){

        // First create the market
        Market market = new Market(
                identifier : parts[5] // :TODO backup position should be 0
        )
        market.save()

        // Import it's data
        MarketImport.importMarket(market)

        // Next the action
        MarketAction marketAction = new MarketAction(
                map:map,
                positionX:Integer.parseInt(parts[0]),
                positionY:Integer.parseInt(parts[1]),
                condition: parts[2]?:null,
                conditionMetMessage: parts[3]?:null,
                conditionNotMetMessage: parts[4]?:null,
                triggerOnActionButton: new Boolean(parts[6]),
                triggerBeforeStep: new Boolean(parts[7]),
                conditionalStep: new Boolean(parts[8]),
                market: market
        )
        map.addToActions(marketAction)
    }

    public static void importNpcAction(def parts, Map map){

        // First create the Npc
        Npc npc = NpcImport.importNpc(parts[5])

        // Next the action
        NpcAction npcAction = new NpcAction(
                map:map,
                positionX:Integer.parseInt(parts[0]),
                positionY:Integer.parseInt(parts[1]),
                condition: parts[2]?:null,
                conditionMetMessage: parts[3]?:null,
                conditionNotMetMessage: parts[4]?:null,
                triggerOnActionButton: new Boolean(parts[6]),
                triggerBeforeStep: new Boolean(parts[7]),
                conditionalStep: new Boolean(parts[8]),
                owner: npc
        )
        npc.npcAction = npcAction
        npcAction.save()
        map.addToActions(npcAction)
    }

    public static void coupleMapTransitions(def parts, Map map){

        MapTransition mapTransition = MapTransition.findByMapAndPositionXAndPositionY(map,Integer.parseInt(parts[0]),Integer.parseInt(parts[1]))
        Map mapTo = Map.findByName(parts[5])

        if (mapTransition){

            MapTransition mapTransitionTo = MapTransition.findByMapAndPositionXAndPositionY(mapTo,Integer.parseInt(parts[6]),Integer.parseInt(parts[7]))

            if (mapTransitionTo){
                mapTransition.jumpTo = mapTransitionTo
                mapTransition.save()
            }
            else {
                println "missing map transition to for mapTransition ${mapTransition.map} ${mapTransition.positionX} ${mapTransition.positionY}"
            }

        }
        else {
            println "missing map transition to for mapTransition ${map} ${parts[0]} ${parts[1]}"
        }
    }

    public static void importMapTransition(def parts, Map map){
        MapTransition mapTransition = new MapTransition(
                map:map,
                positionX:Integer.parseInt(parts[0]),
                positionY:Integer.parseInt(parts[1]),
                condition: parts[2]?:null,
                conditionMetMessage: parts[3]?:null,
                conditionNotMetMessage: parts[4]?:null,
                triggerOnActionButton: new Boolean(parts[8]),
                triggerBeforeStep: new Boolean(parts[9]),
                conditionalStep: new Boolean(parts[10]),
        )
        map.addToActions(mapTransition)
    }

    public static void importRecoverAction(def parts, Map map){

        RecoverAction recoverAction = new RecoverAction(
                map:map,
                positionX:Integer.parseInt(parts[0]),
                positionY:Integer.parseInt(parts[1]),
                condition: parts[2]?:null,
                conditionMetMessage: parts[3]?:null,
                conditionNotMetMessage: parts[4]?:null,
                triggerOnActionButton: new Boolean(parts[5]),
                triggerBeforeStep: new Boolean(parts[6]),
                conditionalStep: new Boolean(parts[7]),
        )
        map.addToActions(recoverAction)

    }

    public static void importPvpSelectAction(def parts, Map map){
        PvpSelectAction pvpSelectAction = new PvpSelectAction(

                map:map,
                positionX:Integer.parseInt(parts[0]),
                positionY:Integer.parseInt(parts[1]),
                condition: parts[2]?:null,
                conditionMetMessage: parts[3]?:null,
                conditionNotMetMessage: parts[4]?:null,
                triggerOnActionButton: new Boolean(parts[5]),
                triggerBeforeStep: new Boolean(parts[6]),
                conditionalStep: new Boolean(parts[7]),
        )
        map.addToActions(pvpSelectAction)
    }

    public static void importComputerAction(def parts, Map map){
            ComputerAction computerAction = new ComputerAction(

                    map:map,
                    positionX:Integer.parseInt(parts[0]),
                    positionY:Integer.parseInt(parts[1]),
                    condition: parts[2]?:null,
                    conditionMetMessage: parts[3]?:null,
                    conditionNotMetMessage: parts[4]?:null,
                    triggerOnActionButton: new Boolean(parts[5]),
                    triggerBeforeStep: new Boolean(parts[6]),
                    conditionalStep: new Boolean(parts[7]),
            )
            map.addToActions(computerAction)
    }

    public static void importMapPokemon(def parts, Map map){

        MapPokemon mapPokemon = new MapPokemon(
                map: map,
                pokemon:Pokemon.get(Integer.parseInt(parts[0])),
                chance : Integer.parseInt(parts[1]),
                fromLevel:Integer.parseInt(parts[2]),
                toLevel:Integer.parseInt(parts[3])
        )
        map.addToMapPokemonList(mapPokemon)
    }

    public static void importMessage(def parts, Map map){

            MapMessage mapMessage = new MapMessage(
                    map:map,
                    positionX:Integer.parseInt(parts[0]),
                    positionY:Integer.parseInt(parts[1]),
                    condition: parts[2]?:null,
                    conditionMetMessage: parts[3]?:null,
                    conditionNotMetMessage: parts[4]?:null,
                    message: parts[5],
                    triggerOnActionButton: new Boolean(parts[6]),
                    triggerBeforeStep: new Boolean(parts[7]),
                    conditionalStep: new Boolean(parts[8]),
            )
            map.addToActions(mapMessage)
    }

}
