package game

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

    def exportMaps(){
        Map.list().each { Map map ->
            String mapPokemonData = map.mapPokemonList.each { it.pokemon.id + ";" + it.chance + ";" + it.fromLevel + ";" + it.toLevel }

//            render text : """
//                ${map.id}<br />
//                ${map.name}<br />
//                ${map.dataBackground}<br />
//                ${map.dataForeground}<br />
//                ${map.active}<br />
//                ${mapPokemonData}<br />
//            """
            exportMap(map)
        }
        render text : "Done"
    }

    public static void exportMap(Map map){
        try{
            File file = new File("import/maps/${map.id} " + map.name + ".txt")
            //println file.getAbsolutePath()
            FileWriter fstream = new FileWriter(file);
            BufferedWriter out = new BufferedWriter(fstream);

            String mapPokemonData = ""
            map.mapPokemonList.each { mapPokemonData += it.pokemon.id + ";" + it.chance + ";" + it.fromLevel + ";" + it.toLevel + ";" }

            String mapTransitions = ""
            String computerActions = ""
            String recoverActions = ""
            String messageActions = ""

            map.actions.each { Action action ->
                if(action in MapTransition){
                    mapTransitions += "${action.id};${action.map.id};${action.positionX};${action.positionY};${action.jumpTo?.id}"
                }
                else if (action in ComputerAction){
                    computerActions += "${action.id};${action.map.id};${action.positionX};${action.positionY};"
                }
                else if (action in RecoverAction){
                    recoverActions += "${action.id};${action.map.id};${action.positionX};${action.positionY};"
                }
                else if (action in MapMessage){
                    messageActions += "${action.id};${action.map.id};${action.positionX};${action.positionY};${action.message};"
                }
            }

out.write("""${map.id}
${map.name}
${map.dataBackground}
${map.dataForeground}
${map.active}
${mapPokemonData}
${mapTransitions}
${computerActions}
${recoverActions}
${messageActions}
""");

            out.close();
        }catch (Exception e){
            System.err.println("Error: " + e.getMessage());
        }
    }
}
