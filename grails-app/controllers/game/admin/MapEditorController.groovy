package game.admin

import game.Map
import game.MapLayout
import data.MapBackup
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import game.context.PlayerData
import game.Player
import game.MapTransition

class MapEditorController {

    def index() {

        def maps = []

        if (SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
            maps = Map.list()
        }
        else {
            PlayerData playerData = session.playerData
            Player player = playerData.getPlayer()

            maps = Map.findAllByOwner(player)
        }

        render view: "index", model: [maps:maps]
    }

    def transitions(){
        Map map

        if (params.id){
            map = Map.get(params.id)

            def maps = MapTransition.findAllByMap(map).collect() { it?.jumpTo?.map }?.unique()

            render view: "transitions", model: [maps:maps]
        }
    }

    def showMap(){

        Map map

        MapLayout mapLayout

        if (params.id){
            map = Map.get(params.id)

            if (map){
                mapLayout = MapLayout.createMapArray(map)
            }

            render text: g.render(template: "/game/layerMap", model: [map: map, mapLayout:mapLayout])

        }
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
            map = Map.get(Integer.parseInt(params.id))
            map.properties = params.map
        }
        else {
            map = new Map(params.map)
        }

        if (!SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
            PlayerData playerData = session.playerData
            Player player = playerData.getPlayer()
            map.owner = player
        }

        map.save()

        // Update images
        map.getForegroundImage(true)
        map.getBackgroundImage(true)

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
