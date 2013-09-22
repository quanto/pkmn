package game.action

import game.context.ActionType
import map.CharacterImage

class CharacterAction extends Action {

    ActionType actionType = ActionType.Client
    String actionFunction = "person"
    String image = "/images/chars/person1.png"
    String cssClass = "actionObject spritely"
    Integer correctionLeft = 0
    Integer correctionTop = -16

    String macro = "llrr"

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

}
