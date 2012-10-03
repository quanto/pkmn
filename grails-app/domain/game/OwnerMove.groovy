package game

class OwnerMove {

    static belongsTo = [ownerPokemon: OwnerPokemon]
    Move move
    int ppLeft

    static constraints = {
    }
}
