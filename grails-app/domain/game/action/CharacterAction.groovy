package game.action

import game.context.ActionType
import map.CharacterImage

class CharacterAction extends Action {

    ActionType actionType = ActionType.Client
    String actionFunction = "person"
    String image = "/images/chars/casualguy1.png"
    String cssClass = "actionObject spritely"
    Integer correctionLeft = 0
    Integer correctionTop = -16
    String initialDirection = "d"
    String macro = ""

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

}
