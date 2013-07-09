package game.action

import game.context.ActionType

class BushAction extends Action {

    ActionType actionType = ActionType.Client
    String actionFunction = "bush"
    String image = "/images/tiles/sheet1/11.png"

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

}
