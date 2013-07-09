package data

import game.Map
import game.MapPokemon
import game.Pokemon
import game.action.*
import game.Npc
import game.Market
import game.Player
import map.CharacterImage

class MapImport {

    public static int totalBaseActionProperties = 13

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
                    else if (line.contains("</boulderAction>")){
                        node = ""
                        importBoulderActions(parts,map)
                        parts = []
                    }
                    else if (line.contains("</bushAction>")){
                        node = ""
                        importBushActions(parts,map)
                        parts = []
                    }
                    else if (line.contains("</findItemAction>")){
                        node = ""
                        importFindItemActionAction(parts,map)
                        parts = []
                    }
                    else if (line.contains("</personAction>")){
                        node = ""
                        importPersonAction(parts,map)
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
                    else if (line.contains("<boulderAction>")){
                        node = "boulderAction"
                    }
                    else if (line.contains("<bushAction>")){
                        node = "bushAction"
                    }
                    else if (line.contains("<findItemAction>")){
                        node = "findItemAction"
                    }
                    else if (line.contains("<personAction>")){
                        node = "personAction"
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
                    coupleMapTransitions(parts)
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

    public static void setMapOwners(){
        new File('import/maps/').listFiles().each { File file ->

            def parts = []
            Map map
            String node
            file.eachLine { line ->

                if (line.contains("</mapData>")){
                    node = ""
                    map = Map.findByName(parts[0])
                    map.owner = parts[6]==''?Player.findByUsername('kevin'):Player.findByUsername(parts[6])
                    map.save()
                    parts = []
                }
                if (node){
                    parts.add(line)
                }
                if (line.contains("<mapData>")){
                    node = "mapData"
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

    public static void addBaseActionProperties(Action action, def parts){
        action.positionX = Integer.parseInt(parts[0])
        action.positionY = Integer.parseInt(parts[1])
        action.identifier = parts[2]
        action.condition = parts[3]?:null
        action.conditionMetMessage = parts[4]?:null
        action.conditionNotMetMessage = parts[5]?:null
        action.triggerOnActionButton = new Boolean(parts[6])
        action.triggerBeforeStep = new Boolean(parts[7])
        action.conditionalStep = new Boolean(parts[8])
        action.placeOneTimeActionLock = new Boolean(parts[9])
        action.image = parts[10]?:null
        action.correctionLeft = parts[11]?Integer.parseInt(parts[11]):null
        action.correctionTop = parts[12]?Integer.parseInt(parts[12]):null
        action.cssClass = parts[13]?:null
    }

    public static void importMarketAction(def parts, Map map){

        // First create the market
        Market market = new Market(
                identifier : parts[totalBaseActionProperties+1]
        )
        market.save()

        // Import it's data
        MarketImport.importMarket(market)

        // Next the action
        MarketAction marketAction = new MarketAction(
                map:map,
                market: market
        )
        addBaseActionProperties(marketAction,parts)

        map.addToActions(marketAction)
    }

    public static void importNpcAction(def parts, Map map){

        // First create the Npc
        Npc npc = NpcImport.importNpc(parts[totalBaseActionProperties+1])

        // Next the action
        NpcAction npcAction = new NpcAction(
                map:map,
                owner: npc
        )
        addBaseActionProperties(npcAction,parts)
        npc.npcAction = npcAction
        npcAction.save()
        map.addToActions(npcAction)
    }

    public static void importPersonAction(def parts, Map map){

        PersonAction action = new PersonAction(
                map:map,
                characterImage: CharacterImage.valueOf(parts[totalBaseActionProperties+1]),
                macro: parts[totalBaseActionProperties+2]
        )
        addBaseActionProperties(action,parts)

        action.save()
        map.addToActions(action)
    }

    public static void coupleMapTransitions(def parts){

        MapTransition mapTransition = MapTransition.findByIdentifier(parts[2])

        if (mapTransition){
            MapTransition mapTransitionTo = MapTransition.findByIdentifier(parts[totalBaseActionProperties+1])

            if (mapTransitionTo){
                mapTransition.jumpTo = mapTransitionTo
                mapTransition.save()
            }
            else {
                println "missing map transition to for mapTransition ${mapTransition.map} ${mapTransition.positionX} ${mapTransition.positionY}"
            }

        }
        else {
            println "missing map transition to for mapTransition ${parts[2]} ${parts[0]} ${parts[1]}"
        }
    }

    public static void importMapTransition(def parts, Map map){
        MapTransition mapTransition = new MapTransition(
                map:map,
        )
        addBaseActionProperties(mapTransition,parts)
        map.addToActions(mapTransition)
    }

    public static void importRecoverAction(def parts, Map map){

        RecoverAction recoverAction = new RecoverAction(
                map:map,
        )
        addBaseActionProperties(recoverAction,parts)

        map.addToActions(recoverAction)

    }

    public static void importPvpSelectAction(def parts, Map map){
        PvpSelectAction pvpSelectAction = new PvpSelectAction(
                map:map,
        )
        addBaseActionProperties(pvpSelectAction,parts)
        map.addToActions(pvpSelectAction)
    }

    public static void importFindItemActionAction(def parts, Map map){
        FindItemAction action = new FindItemAction(

                map:map,
        )
        addBaseActionProperties(action,parts)
        map.addToActions(action)
    }

    public static void importBushActions(def parts, Map map){
        BushAction action = new BushAction(

                map:map,
        )
        addBaseActionProperties(action,parts)
        map.addToActions(action)
    }

    public static void importBoulderActions(def parts, Map map){
        BoulderAction action = new BoulderAction(

                map:map,
        )
        addBaseActionProperties(action,parts)
        map.addToActions(action)
    }

    public static void importComputerAction(def parts, Map map){
            ComputerAction computerAction = new ComputerAction(

                    map:map,
            )
            addBaseActionProperties(computerAction,parts)
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
                    message: parts[totalBaseActionProperties+1],
            )
            addBaseActionProperties(mapMessage,parts)
            map.addToActions(mapMessage)
    }

}
