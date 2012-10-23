package game.admin

import game.Map
import game.MapLayout
import game.MapTransition
import game.Action
import game.ComputerAction
import game.RecoverAction
import game.MapMessage
import game.NpcAction
import data.OwnerBackup
import game.MarketAction
import data.MarketBackup
import data.MapBackup

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
            render text: MapBackup.exportMap(map).replace('\n','<br />')
        }

    }

    def exportMaps(){
        Map.list().each { Map map ->
            MapBackup.exportMap(map)
        }
        render text : "Done"
    }

}
