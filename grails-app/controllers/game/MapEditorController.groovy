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
}
