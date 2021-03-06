package game

import game.action.Action

import javax.imageio.ImageIO
import scaffolding.ScaffoldLink

class Map {

    String name
    String dataForeground
    String dataBackground
    boolean active
    Integer worldX
    Integer worldY

    Player owner

    static scaffoldList           = ['name','worldX','worldY','owner.username','mapEditor','pokemon','showMap']
    static scaffoldSearch         = ['name','worldX','worldY','owner.username']

    static hasMany = [mapPokemonList: MapPokemon, actions:Action, altMaps: AltMap]

    static mapping = {
        dataForeground type:"text"
        dataBackground type:"text"
    }

    static constraints = {
        id display: true
    //    dataForeground display:false
    //    dataBackground display:false
    //    pokemon display:false
        active display:true
        worldX nullable: true
        worldY nullable: true
        owner nullable: true
    }

    static transients = ['foregroundImage','backgroundImage','mapEditor','pokemon','showMap']

    public ScaffoldLink getMapEditor(){
        new ScaffoldLink(link:"../../mapEditor/editor/" + this.id , tekst: "Edit")
    }

    public ScaffoldLink getPokemon(){
        new ScaffoldLink(link:"../../pokemonEditor/edit/" + this.id , tekst: "PKMN(${mapPokemonList?.size()})")
    }

    public ScaffoldLink getShowMap(){
        new ScaffoldLink(link:"../../mapEditor/showMap/" + this.id , tekst: "Show")
    }

    @Override
    public String toString(){
        return "Map: ${id} ${name}"
    }

    public String getForegroundImage(AltMap altMap, boolean forceUpdate = false){
        File file

        String altMapPostFix = altMap?altMap.priority+"_":""

        String filePath = "/images/generatedMaps/${name}_${altMapPostFix}background.png"

        try {
            file = new File("web-app" + filePath)
            if (!file.exists() || forceUpdate){
                MapLayout mapLayout = MapLayout.createMapArray(this, altMap)
                ImageIO.write(mapLayout.writeTiles(mapLayout.background), "png", file)
            }

        } catch (IOException e) {
            println "Failed to convert background map to png"
        }
        return filePath
    }

    public String getBackgroundImage(AltMap altMap, boolean forceUpdate = false){
        File file

        String altMapPostFix = altMap?altMap.priority+"_":""

        String filePath = "/images/generatedMaps/${name}_${altMapPostFix}foreground.png"

        try {
            file = new File("web-app" + filePath)
            if (!file.exists() || forceUpdate){
                MapLayout mapLayout = MapLayout.createMapArray(this, altMap)
                ImageIO.write(mapLayout.writeTiles(mapLayout.foreground), "png", file)
            }

        } catch (IOException e) {
            println "Failed to convert foreground map to png"
        }
        return filePath
    }


}
