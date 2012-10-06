package game

class Map {

    String name
    String dataForeground
    String dataBackground
    boolean active

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
    }

    @Override
    public String toString(){
        return "Map: ${id} ${name}"
    }

}
