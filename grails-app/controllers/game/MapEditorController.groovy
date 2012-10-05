package game

class MapEditorController {

    def index() {
        render view: "index", model: [maps:Map.list()]
    }


    def editor(){
        render view: "editor" //, model: [map:Map.get(Integer.parseInt(params.id))]
    }
}
