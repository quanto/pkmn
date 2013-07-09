package game.action

import game.context.ActionType

class BoulderAction extends Action {

    String actionFunction = "boulder"
    String image = "/images/tiles/sheet1/32.png"
    ActionType actionType = ActionType.Client

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

    static constraints = {
    }
}
