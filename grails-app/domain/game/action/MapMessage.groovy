package game.action

import game.context.ActionType

class MapMessage extends Action {

    ActionType actionType = ActionType.Server

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

    String message

    static constraints = {

    }
}
