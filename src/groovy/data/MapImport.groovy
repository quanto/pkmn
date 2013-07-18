package data

import game.AltMap
import game.Condition
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

            new File('import/maps/').listFiles().each { File file ->

                Map map
                AltMap altMap

                String node = ""

                def parts = []

                file.eachLine { line ->

                    if (line.contains("</mapData>")){
                        node = ""
                        map = createMap(parts)
                        parts = []
                    }
                    else if (line.contains("</altMapData>")){
                        node = ""
                        altMap = createAltMap(parts,map)
                        parts = []
                    }
                    else if (line.contains("</marketAction>")){
                        node = ""
                        importMarketAction(parts, map, altMap)
                        parts = []
                    }
                    else if (line.contains("</npcAction>")){
                        node = ""
                        importNpcAction(parts, map, altMap)
                        parts = []
                    }
                    else if (line.contains("</mapMessage>")){
                        node = ""
                        importMessage(parts, map, altMap)
                        parts = []
                    }
                    else if (line.contains("</recoverAction>")){
                        node = ""
                        importRecoverAction(parts,map, altMap)
                        parts = []
                    }
                    else if (line.contains("</pvpSelectAction>")){
                        node = ""
                        importPvpSelectAction(parts,map, altMap)
                        parts = []
                    }
                    else if (line.contains("</computerAction>")){
                        node = ""
                        importComputerAction(parts,map, altMap)
                        parts = []
                    }
                    else if (line.contains("</mapTransition>")){
                        node = ""
                        importMapTransition(parts,map, altMap)
                        parts = []
                    }
                    else if (line.contains("</boulderAction>")){
                        node = ""
                        importBoulderActions(parts,map, altMap)
                        parts = []
                    }
                    else if (line.contains("</bushAction>")){
                        node = ""
                        importBushActions(parts,map, altMap)
                        parts = []
                    }
                    else if (line.contains("</findItemAction>")){
                        node = ""
                        importFindItemActionAction(parts,map, altMap)
                        parts = []
                    }
                    else if (line.contains("</personAction>")){
                        node = ""
                        importPersonAction(parts,map, altMap)
                        parts = []
                    }
                    else if (line.contains("</messagePersonAction>")){
                        node = ""
                        importMessagePersonAction(parts,map, altMap)
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
                    else if (line.contains("<altMapData>")){
                        node = "altMapData"
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
                    else if (line.contains("<messagePersonAction>")){
                        node = "messagePersonAction"
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

    public static AltMap createAltMap(def parts, Map map){
        AltMap altMap = new AltMap(
                newDataBackground : new Boolean(parts[0]),
                newDataForeground : new Boolean(parts[1]),
                newActions : new Boolean(parts[2]),
                dataBackground : parts[3]?:null,
                dataForeground : parts[4]?:null,
                condition : parts[5],
                priority : Integer.parseInt(parts[6]),
                map: map
        )
        altMap.save()

        return altMap
    }

    public static void addMapOrAltMap(Action action, Map map, AltMap altMap){
        if (altMap){
            action.altMap = altMap
            altMap.addToActions(action)
        }
        else {
            action.map = map
            map.addToActions(action)
        }
    }


    public static void addBaseActionProperties(Action action, def parts){
        action.positionX = Integer.parseInt(parts[0])
        action.positionY = Integer.parseInt(parts[1])
        action.identifier = parts[2]
        action.condition = parts[3]? Condition.valueOf(parts[3]):null
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

    public static void importMarketAction(def parts, Map map, AltMap altMap){

        // First create the market
        Market market = new Market(
                identifier : parts[totalBaseActionProperties+1]
        )
        market.save()

        // Import it's data
        MarketImport.importMarket(market)

        // Next the action
        MarketAction action = new MarketAction(
                market: market
        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)
    }

    public static void importNpcAction(def parts, Map map, AltMap altMap){

        // First create the Npc
        Npc npc = NpcImport.importNpc(parts[totalBaseActionProperties+1])

        // Next the action
        NpcAction action = new NpcAction(
                owner: npc
        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)
        npc.npcAction = action
        action.save()
    }



    public static void importMessagePersonAction(def parts, Map map, AltMap altMap){

        MessagePersonAction action = new MessagePersonAction(
                characterImage: CharacterImage.valueOf(parts[totalBaseActionProperties+1]),
                macro: parts[totalBaseActionProperties+2],
                message: parts[totalBaseActionProperties+3]
        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)

        action.save()
    }

    public static void importPersonAction(def parts, Map map, AltMap altMap){

        PersonAction action = new PersonAction(
                characterImage: CharacterImage.valueOf(parts[totalBaseActionProperties+1]),
                macro: parts[totalBaseActionProperties+2]
        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)

        action.save()
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

    public static void importMapTransition(def parts, Map map, AltMap altMap){
        MapTransition mapTransition = new MapTransition(
                map:map,
        )
        addBaseActionProperties(mapTransition,parts)
        map.addToActions(mapTransition)
    }

    public static void importRecoverAction(def parts, Map map, AltMap altMap){

        RecoverAction action = new RecoverAction(

        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)

    }

    public static void importPvpSelectAction(def parts, Map map, AltMap altMap){
        PvpSelectAction action = new PvpSelectAction(

        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)
    }

    public static void importFindItemActionAction(def parts, Map map, AltMap altMap){
        FindItemAction action = new FindItemAction(

        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)
    }

    public static void importBushActions(def parts, Map map, AltMap altMap){
        BushAction action = new BushAction(

        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)
    }

    public static void importBoulderActions(def parts, Map map, AltMap altMap){
        BoulderAction action = new BoulderAction(

        )
        addBaseActionProperties(action,parts)
        addMapOrAltMap(action, map, altMap)
    }

    public static void importComputerAction(def parts, Map map, AltMap altMap){
        ComputerAction action = new ComputerAction(

        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)
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

    public static void importMessage(def parts, Map map, AltMap altMap){

        MapMessage action = new MapMessage(
                message: parts[totalBaseActionProperties+1],
        )
        addMapOrAltMap(action, map, altMap)
        addBaseActionProperties(action,parts)
    }

}
