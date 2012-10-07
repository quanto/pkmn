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

            render text : """
                ${map.id}<br />
                ${map.name}<br />
                ${map.dataBackground}<br />
                ${map.dataForeground}<br />
                ${map.active}<br />
                ${mapPokemonData}<br />
            """
        }
    }
}
