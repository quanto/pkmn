package game

class MapPokemon {

    static belongsTo = [map: Map]
    Pokemon pokemon
    int chance
    int fromLevel
    int toLevel

    static constraints = {
    }

    public String toString(){
        return "MapPokemon: ${pokemon.toString()} lvl. ${fromLevel}-${toLevel}"
    }

}
