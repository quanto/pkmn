package game.action

import game.action.Action
import game.context.ActionType

class BushAction extends Action {

    ActionType actionType = ActionType.Client
    String actionFunction = "bush"
    String tileImage = "11"

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

}
