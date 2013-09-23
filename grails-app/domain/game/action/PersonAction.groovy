package game.action

import game.context.ActionType
import map.CharacterImage

class PersonAction extends CharacterAction{

    String cssClass = "actionObject spritely"
    Integer correctionLeft = 0
    Integer correctionTop = -16

    CharacterImage characterImage = CharacterImage.casualguy1

    public String getImage(){
        if (!characterImage){
            return null
        }

        return "/images/chars/${characterImage.name()}.png"
    }

}
