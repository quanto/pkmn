package game.action

import game.action.Action
import game.context.ActionType

class BoulderAction extends Action {

    String actionFunction = "boulder"
    String tileImage = "32"
    ActionType actionType = ActionType.Client

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

    static constraints = {
    }
}
