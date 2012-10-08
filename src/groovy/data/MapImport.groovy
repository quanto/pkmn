package data

import game.Map
import game.MapMessage
import game.MapPokemon
import game.Pokemon
import game.ComputerAction
import game.RecoverAction
import game.MapTransition

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 04-10-12
 * Time: 09:37
 * To change this template use File | Settings | File Templates.
 */
class MapImport {

    public static void importMaps(){
        println "Import map lines"

        boolean newImport = true

        if (!newImport){

            def file = new File('import/maps.txt')

            int index = 0
            def parts = []
            file.eachLine { line ->
                parts.add( line )
                if (index%4==3){

                    Map map = new Map(
                            name : parts[0],
                            dataForeground : parts[1],
                            dataBackground : parts[2],
                            active : parts[3] == '1'
                    )

                    map.save()

                    parts = []

                }
                index++
            }
        }
        else {

            Map.withTransaction {
                new File('import/maps/').listFiles().each { File file ->

                    int index = 0
                    def parts = []
                    file.eachLine { line ->
                        parts.add( line )
                        if (index%10==9){
                            Map map = new Map(
                                    id : Integer.parseInt(parts[0]),
                                    name : parts[1],
                                    dataForeground : parts[3],
                                    dataBackground : parts[2],
                                    active : parts[4] == '1'
                            )

                            map.save()

                            String mapPokemonData = parts[5]
                            def mapPokemonParts = mapPokemonData.split(';')
                            importMapPokemon(mapPokemonParts,map)

                            String mapTransitions = parts[6]
                            def mapTransitionParts = mapTransitions.split(';')
                            importMapTransitions(mapTransitionParts,map)

                            String computerActions = parts[7]
                            def computerActionParts = computerActions.split(';')
                            importComputerActions(computerActionParts,map)

                            String recoverActions = parts[8]
                            def recoverActionParts = recoverActions.split(';')
                            importRecoverActions(recoverActionParts,map)

                            String messageActions = parts[9]
                            def messageParts = messageActions.split(';')
                            importMessages(messageParts, map)

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
                        Map map = Map.findByName(parts[1])
                        String mapTransitions = parts[6]
                        def mapTransitionParts = mapTransitions.split(';')
                        coupleMapTransitions(mapTransitionParts,map)
                        parts = []
                    }
                    index++
                }

            }
        }
    }

    public static void coupleMapTransitions(def parts, Map map){
        int total = Math.floor(parts.size() / 5)
        for (int i=0; i<total;i++){
            int s = i*5

            MapTransition mapTransition = MapTransition.findByMapAndPositionXAndPositionY(map,Integer.parseInt(parts[s+0]),Integer.parseInt(parts[s+1]))
            Map mapTo = Map.findByName(parts[s+2])
            println mapTo.name
            println parts[s+2]
            println parts[s+3]
            println parts[s+4]
            MapTransition mapTransitionTo = MapTransition.findByMapAndPositionXAndPositionY(mapTo,Integer.parseInt(parts[s+3]),Integer.parseInt(parts[s+4]))
            println mapTransitionTo
            mapTransition.jumpTo = mapTransitionTo
            mapTransition.save()
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
