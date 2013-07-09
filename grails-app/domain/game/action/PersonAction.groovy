package game.action

import game.context.ActionType

class PersonAction extends Action{

    ActionType actionType = ActionType.Client
    String actionFunction = "person"
    String image = "/game/images/chars/person1.png"
    String macro = "llrr"

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

}
