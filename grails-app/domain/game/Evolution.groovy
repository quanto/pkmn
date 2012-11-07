package game

class Evolution {

    Pokemon fromPokemon
    Pokemon toPokemon
    int level
    String condition

    static mapping = {
        condition column: "`condition`" // Condition is a reserved keyword in mysql
    }

}
