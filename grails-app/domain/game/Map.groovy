package game

import javax.imageio.ImageIO

class Map {

    String name
    String dataForeground
    String dataBackground
    boolean active
    Integer worldX
    Integer worldY

    static hasMany = [mapPokemonList: MapPokemon, actions:Action]

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
    }

    static transients = ['foregroundImage','backgroundImage']

    @Override
    public String toString(){
        return "Map: ${id} ${name}"
    }

    public String getForegroundImage(boolean forceUpdate = false){
        File file

        String filePath = "/images/generatedMaps/${name}_background.png"

        try {
            file = new File("web-app" + filePath)
            if (!file.exists() || forceUpdate){
                MapLayout mapLayout = MapLayout.createMapArray(this)
                ImageIO.write(mapLayout.writeTiles(mapLayout.background), "png", file)
            }

        } catch (IOException e) {
            println "Failed to convert background map to png"
        }
        return filePath
    }

    public String getBackgroundImage(boolean forceUpdate = false){
        File file

        String filePath = "/images/generatedMaps/${name}_foreground.png"

        try {
            file = new File("web-app" + filePath)
            if (!file.exists() || forceUpdate){
                MapLayout mapLayout = MapLayout.createMapArray(this)
                ImageIO.write(mapLayout.writeTiles(mapLayout.foreground), "png", file)
            }

        } catch (IOException e) {
            println "Failed to convert foreground map to png"
        }
        return filePath
    }


}
