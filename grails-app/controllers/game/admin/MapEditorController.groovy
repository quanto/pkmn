package game.admin

import game.AltMap
import game.Condition
import game.Map
import game.MapLayout
import data.MapBackup
import grails.plugin.springsecurity.SpringSecurityUtils
import game.context.PlayerData
import game.Player
import game.action.MapTransition

class MapEditorController {

    def index() {

        def maps = []

        if (SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
            //maps = Map.list()
            maps = Map.findAllByWorldXIsNull()

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
                AltMap altMap = params.altMap?:AltMap.get(params.altMap)
                mapLayout = MapLayout.createMapArray(map,altMap)
            }

            render text: g.render(template: "/game/layerMap", model: [map: map, mapLayout:mapLayout])
        }
    }


    def editor(long id, long altMapId){

        Map map

        MapLayout mapLayout

        AltMap altMap

        if (id){
            map = Map.get(id)

            if (map){
                altMap = altMapId?AltMap.get(altMapId):null
                mapLayout = MapLayout.createMapArray(map, altMap)
            }
        }
        render view: "editor", model: [map: map, mapLayout: mapLayout, altMap: altMap]
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

    def saveMap(long id, long altMapId){
        Map map
        AltMap altMap

        if (id){

            map = Map.get(id)
            map.properties = params.map

            assert map

            if (altMapId){
                altMap = AltMap.get(altMapId)
                assert altMap

                if (altMap.newDataForeground){
                    altMap.dataForeground = params.dataForeground
                }

                if (altMap.newDataBackground){
                    altMap.dataBackground = params.dataBackground
                }

            }
            else {
                map.dataForeground = params.dataForeground
                map.dataBackground = params.dataBackground
            }

        }
        else {
            map = new Map(params.map)
            map.dataForeground = params.dataForeground
            map.dataBackground = params.dataBackground
        }

        if (!SpringSecurityUtils.ifAllGranted('ROLE_ADMIN')){
            PlayerData playerData = session.playerData
            Player player = playerData.getPlayer()
            map.owner = player
        }

        map.save()
        altMap?.save()

        // Update images
        map.getForegroundImage(altMap, true)
        map.getBackgroundImage(altMap, true)

        redirect action:"editor", id: map.id, params : [altMapId: altMap?.id]
    }

    def clone(long id){
        Map map = Map.get(id)

        Map newMap = new Map()
        newMap.dataBackground = map.dataBackground
        newMap.dataForeground = map.dataForeground
        newMap.name = map.name + " (clone)"
        newMap.save(flush:true)
        redirect action:"editor", id: newMap.id
    }

    def delete(long id){
        Map map = Map.get(id)
        map.delete()
        redirect action:'index'
    }

    def altMap(long id, long altMapId){

        AltMap altMap = AltMap.get(altMapId)

        render view: "altMap", model:[mapId: id, altMap: altMap]
    }

    def createAltMap(long altMapId, long mapId, boolean newDataBackground, boolean newDataForeground, boolean newActions, String condition){
        Map map = Map.get(mapId)
        assert map

        AltMap altMap = AltMap.get(altMapId)

        if (altMap){
            if (altMap.newDataBackground != newDataBackground){
                altMap.newDataBackground = newDataBackground
                altMap.dataBackground = altMap.newDataBackground?map.dataBackground:null
            }
            if (altMap.newDataForeground != newDataForeground){
                altMap.newDataForeground = newDataForeground
                altMap.dataForeground = altMap.newDataForeground?map.dataForeground:null
            }
            if (altMap.newActions != newActions){
                altMap.newActions = newActions
                altMap.actions = altMap.newActions?[]:null
            }
            altMap.condition = condition?Condition.valueOf(condition):null
        }
        else {
            altMap = new AltMap(params)
            altMap.map = map
            map.addToAltMaps(altMap)
            altMap.dataForeground = altMap.newDataForeground?map.dataForeground:null
            altMap.dataBackground = altMap.newDataBackground?map.dataBackground:null
            altMap.actions = altMap.newActions?[]:null
        }

        altMap.save()

        // :TODO Show message
        flash.message = "AltMap aangemaakt"

        redirect action: 'worldMap'
    }

    def listCharacters(){

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

    def place(long id){
        Map map = Map.get(id)
        AltMap altMap = null
        MapLayout mapLayout = MapLayout.createMapArray(map, altMap)
        render view : 'place', model: [map:map,altMap:altMap,mapLayout: mapLayout]
    }

    def placeMe(long mapId, int x, int y){
        Map map = Map.get(mapId)

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()
        player.map = map
        player.altMap = null
        player.positionX = x
        player.positionY = y
        player.save(flush: true)
        redirect controller:'game'
    }

}
