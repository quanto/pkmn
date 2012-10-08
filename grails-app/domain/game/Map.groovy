package game

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

    @Override
    public String toString(){
        return "Map: ${id} ${name}"
    }

}
