package game

import game.context.ActionType

class MapMessage extends Action {

    ActionType actionType = ActionType.Server

    String message

    static constraints = {

    }
}
