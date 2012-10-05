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
            mapLayout = MapLayout.createMapArray(map);
        }
        render view: "editor", model: [mapLayout:mapLayout]
    }
}
