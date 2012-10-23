package game

class MapTransition extends Action {

    // Defining no jumpTo means there's no way back
    MapTransition jumpTo

    String condition // Hardcoded condition
    String conditionNotMetMessage

    static constraints = {
        jumpTo nullable: true
        condition nullable: true
        conditionNotMetMessage nullable: true
    }
}
