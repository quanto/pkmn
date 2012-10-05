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
        // :TODO set name and active
        Map map

        if (params.mapId){
            map = Map.get(params.mapId)
            if (map){
                map.dataBackground = params.background
                map.dataForeground = params.foreground
                map.save()
            }
        }
        else {
            map = new Map(
                    dataBackground: params.background,
                    dataForeground: params.foreground
            )
        }

        println params

        redirect action:"index"
    }
}
