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

class MapImport {

    public static void importMaps(){
        println "Import map lines"

        Map.withTransaction {
            new File('import/maps/').listFiles().each { File file ->

                int index = 0
                def parts = []
                file.eachLine { line ->
                    parts.add( line )
                    if (index%13==12){
                        Map map = new Map(
                                name : parts[0],
                                dataForeground : parts[2],
                                dataBackground : parts[1],
                                active : parts[3] == '1',
                                worldX: parts[9]=='null'?null:parts[9],
                                worldY: parts[10]=='null'?null:parts[10]
                        )

                        map.save()

                        String mapPokemonData = parts[4]
                        def mapPokemonParts = mapPokemonData.split(';')
                        importMapPokemon(mapPokemonParts,map)

                        String mapTransitions = parts[5]
                        def mapTransitionParts = mapTransitions.split(';')
                        importMapTransitions(mapTransitionParts,map)

                        String computerActions = parts[6]
                        def computerActionParts = computerActions.split(';')
                        importComputerActions(computerActionParts,map)

                        String recoverActions = parts[7]
                        def recoverActionParts = recoverActions.split(';')
                        importRecoverActions(recoverActionParts,map)

                        String messageActions = parts[8]
                        def messageParts = messageActions.split(';')
                        importMessages(messageParts, map)

                        String npcActions = parts[11]
                        def npcParts = npcActions.split(';')
                        importNpcActions(npcParts, map)

                        String marketActions = parts[12]
                        def marketParts = marketActions.split(';')
                        importMarketActions(marketParts, map)

                        parts = []

                    }
                    index++
                }

            }
        }

        new File('import/maps/').listFiles().each { File file ->
            int index = 0
            def parts = []
            file.eachLine { line ->
                parts.add( line )
                if (index%10==9){
                    Map map = Map.findByName(parts[0])
                    String mapTransitions = parts[5]
                    def mapTransitionParts = mapTransitions.split(';')
                    coupleMapTransitions(mapTransitionParts,map)
                    parts = []
                }
                index++
            }

        }

    }

    public static void importMarketActions(def parts, Map map){
        int total = Math.floor(parts.size() / 3)
        for (int i=0; i<total;i++){
            int s = i*3

            // First create the market
            Market market = new Market(
                    identifier : parts[s+2]
            )
            market.save()

            // Import it's data
            MarketImport.importMarket(market)

            // Next the action
            MarketAction marketAction = new MarketAction(
                    map:map,
                    positionX:Integer.parseInt(parts[s+0]),
                    positionY:Integer.parseInt(parts[s+1]),
                    market: market
            )
            map.addToActions(marketAction)
        }
    }

    public static void importNpcActions(def parts, Map map){
        int total = Math.floor(parts.size() / 4)
        for (int i=0; i<total;i++){
            int s = i*4

            // First create the NPC
            Npc npc = new Npc(
                    identifier : parts[s+2],
                    name: parts[s+3],
            )
            npc.save()

            // Import it's data
            NpcImport.importNpc(npc)

            // Next the action
            NpcAction npcAction = new NpcAction(
                    map:map,
                    positionX:Integer.parseInt(parts[s+0]),
                    positionY:Integer.parseInt(parts[s+1]),
                    owner: npc
            )
            npc.npcAction = npcAction
            npcAction.save()
            map.addToActions(npcAction)
        }
    }

    public static void coupleMapTransitions(def parts, Map map){
        int total = Math.floor(parts.size() / 5)
        for (int i=0; i<total;i++){
            int s = i*5

            MapTransition mapTransition = MapTransition.findByMapAndPositionXAndPositionY(map,Integer.parseInt(parts[s+0]),Integer.parseInt(parts[s+1]))
            Map mapTo = Map.findByName(parts[s+2])

            if (mapTransition){

                MapTransition mapTransitionTo = MapTransition.findByMapAndPositionXAndPositionY(mapTo,Integer.parseInt(parts[s+3]),Integer.parseInt(parts[s+4]))

                if (mapTransitionTo){
                    mapTransition.jumpTo = mapTransitionTo
                    mapTransition.save()
                }
                else {
                    println "missing map transition to for mapTransition ${mapTransition.map} ${mapTransition.positionX} ${mapTransition.positionY}"
                }

            }
            else {
                println "missing map transition to for mapTransition ${map} ${parts[s+0]} ${parts[s+1]}"
            }
        }
    }

    public static void importMapTransitions(def parts, Map map){
        int total = Math.floor(parts.size() / 5)
        for (int i=0; i<total;i++){
            int s = i*5

            MapTransition mapTransition = new MapTransition(
                    map:map,
                    positionX:Integer.parseInt(parts[s+0]),
                    positionY:Integer.parseInt(parts[s+1]),
            )
            map.addToActions(mapTransition)
        }
    }

    public static void importRecoverActions(def parts, Map map){
        int total = Math.floor(parts.size() / 2)
        for (int i=0; i<total;i++){
            int s = i*2

            RecoverAction recoverAction = new RecoverAction(
                    map:map,
                    positionX:Integer.parseInt(parts[s+0]),
                    positionY:Integer.parseInt(parts[s+1]),
            )
            map.addToActions(recoverAction)
        }
    }

    public static void importComputerActions(def parts, Map map){
        int total = Math.floor(parts.size() / 2)
        for (int i=0; i<total;i++){
            int s = i*2

            ComputerAction computerAction = new ComputerAction(

                    map:map,
                    positionX:Integer.parseInt(parts[s+0]),
                    positionY:Integer.parseInt(parts[s+1]),
            )
            map.addToActions(computerAction)
        }
    }

    public static void importMapPokemon(def parts, Map map){
        int total = Math.floor(parts.size() / 4)
        for (int i=0; i<total;i++){
            int s = i*4

            MapPokemon mapPokemon = new MapPokemon(
                    map: map,
                    pokemon:Pokemon.get(Integer.parseInt(parts[s+0])),
                    chance : Integer.parseInt(parts[s+1]),
                    fromLevel:Integer.parseInt(parts[s+2]),
                    toLevel:Integer.parseInt(parts[s+3])
            )
            map.addToMapPokemonList(mapPokemon)
        }
    }

    public static void importMessages(def messageParts, Map map){
        int total = Math.floor(messageParts.size() / 3)
        for (int i=0; i<total;i++){
            int s = i*3

            MapMessage mapMessage = new MapMessage(
                    map:map,
                    positionX:Integer.parseInt(messageParts[s+0]),
                    positionY:Integer.parseInt(messageParts[s+1]),
                    message: messageParts[s+2]
            )
            map.addToActions(mapMessage)
        }
    }

}
