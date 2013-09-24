package game.action

import game.Npc
import game.action.Action
import game.context.ActionType
import map.CharacterImage

class NpcAction extends CharacterAction {

    boolean triggerBeforeStep = true
    ActionType actionType = ActionType.Mixed
    String cssClass = "actionObject spritely"
    Integer correctionLeft = 0
    Integer correctionTop = -16
    Npc owner
    CharacterImage characterImage = CharacterImage.casualguy1
    String actionFunction = "npc"
    String initialDirection = "d"
    String macro = "" // Should not contain a macro
    String message = "" // message pre-battle

    public String getImage(){
        if (!characterImage){
            return null
        }
        return "/images/chars/${characterImage.name()}.png"
    }

}
