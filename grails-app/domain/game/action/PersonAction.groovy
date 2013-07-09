package game.action

import game.context.ActionType
import map.CharacterImage

class PersonAction extends Action{

    ActionType actionType = ActionType.Client
    String actionFunction = "person"
    String image = "/game/images/chars/person1.png"

    CharacterImage characterImage = CharacterImage.person1
    String macro = "llrr"

    public String getImage(){
        if (!characterImage){
            return null
        }

        return "/game/images/chars/${characterImage.name()}.png"
    }

    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

}
