package game.admin

import game.Map
import game.MapLayout
import game.MapTransition
import game.Action
import game.ComputerAction
import game.RecoverAction
import game.MapMessage

class MapEditorController {

    def index() {
        render view: "index", model: [maps:Map.list()]
    }


    def editor(){

        Map map

        MapLayout mapLayout

        if (params.id){
            map = Map.get(params.id)

            if (map){
                mapLayout = MapLayout.createMapArray(map)
            }
        }
        render view: "editor", model: [map: map, mapLayout:mapLayout]
    }

    def worldMap = {
        def model = [:]

        List<Map> worldMaps = Map.findAllByWorldXIsNotNullAndWorldYIsNotNull()
        model.worldMaps = worldMaps
        model.lowestX = worldMaps.min{ it.worldX }.worldX
        model.lowestY = worldMaps.min{ it.worldY }.worldY
        model.highestX = worldMaps.max{ it.worldX }.worldX
        model.highestY = worldMaps.max{ it.worldY }.worldY

        render view: "worldMap", model:model
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

    def saveMap(){
        Map map
        if (params.id){
            map = Map.get(params.id)
            map.properties = params.map
        }
        else {
            map = new Map(params.map)
        }

        map.save()

        redirect action:"index"
    }

    def export(){
        Map map
        if (params.id){
            map = Map.get(params.id)
            render text: exportMap(map).replace('\n','<br />')
        }

    }

    def exportMaps(){
        Map.list().each { Map map ->
            String mapPokemonData = map.mapPokemonList.each { it.pokemon.id + ";" + it.chance + ";" + it.fromLevel + ";" + it.toLevel }

            exportMap(map)
        }
        render text : "Done"
    }

    public static String exportMap(Map map){

        String data = ""

        try{
            File file = new File("import/maps/" + map.name + ".txt")
            //println file.getAbsolutePath()
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);

            String mapPokemonData = ""
            map.mapPokemonList.each { mapPokemonData += it.pokemon.id + ";" + it.chance + ";" + it.fromLevel + ";" + it.toLevel + ";" }

            String mapTransitions = ""
            String computerActions = ""
            String recoverActions = ""
            String messageActions = ""

            // Sort so we always have the same order in the actions
            map.actions.sort { it.positionX + "" + it.positionY } .each { Action action ->
                if(action in MapTransition){
                    if (action.jumpTo)
                        mapTransitions += "${action.positionX};${action.positionY};${action.jumpTo?.map?.name};${action.jumpTo?.positionX};${action.jumpTo?.positionY};"
                }
                else if (action in ComputerAction){
                    computerActions += "${action.positionX};${action.positionY};"
                }
                else if (action in RecoverAction){
                    recoverActions += "${action.positionX};${action.positionY};"
                }
                else if (action in MapMessage){
                    messageActions += "${action.positionX};${action.positionY};${action.message};"
                }
            }

data = """${map.id}
${map.name}
${map.dataBackground}
${map.dataForeground}
${map.active}
${mapPokemonData}
${mapTransitions}
${computerActions}
${recoverActions}
${messageActions}
${map.worldX}
${map.worldY}
"""
            out.write(data)

            out.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }


        return data
    }
}
