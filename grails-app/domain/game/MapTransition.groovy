package game

class MapTransition {

    int positionX
    int positionY
    Map map

    // Defining no jumpTo means there's no way back
    MapTransition jumpTo

    static constraints = {
        jumpTo nullable: true
    }
}
