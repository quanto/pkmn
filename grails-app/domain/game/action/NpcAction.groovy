package game.action

import game.Npc
import game.action.Action
import game.context.ActionType
import map.CharacterImage

class NpcAction extends Action {

    boolean triggerBeforeStep = true
    ActionType actionType = ActionType.Mixed
    String cssClass = "actionObject spritely"
    Integer correctionLeft = 0
    Integer correctionTop = -16
    Npc owner
    CharacterImage characterImage = CharacterImage.casualguy1
    String actionFunction = "npc"

    public String getImage(){
        if (!characterImage){
            return null
        }
        return "/images/chars/${characterImage.name()}.png"
    }

}
