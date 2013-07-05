package game.action

import game.context.ActionType

class MapTransition extends Action {

    ActionType actionType = ActionType.Server

    // Defining no jumpTo means there's no way back
    MapTransition jumpTo

    static constraints = {
        jumpTo nullable: true
    }
}
