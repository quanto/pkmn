package game

class MapTransition extends ServerAction {

    // Defining no jumpTo means there's no way back
    MapTransition jumpTo

    static constraints = {
        jumpTo nullable: true
    }
}
