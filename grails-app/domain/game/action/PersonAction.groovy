package game.action

import game.context.ActionType

class PersonAction extends Action{

    ActionType actionType = ActionType.Client
    String actionFunction = "person"
    String tileImage = "11"
    String macro = "llrr"

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

}
