package game

class MapTransition extends Action {

    // Defining no jumpTo means there's no way back
    MapTransition jumpTo

    static constraints = {
        jumpTo nullable: true
    }
}
