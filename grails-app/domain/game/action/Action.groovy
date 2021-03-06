package game.action

import game.AltMap
import game.Condition
import org.apache.commons.lang.RandomStringUtils
import game.context.ActionType

class Action {

    ActionType actionType

    String actionFunction // The js function to call to perform the action

    String image // The image to show frontend
    Integer correctionLeft // Correction on the image
    Integer correctionTop
    String cssClass

    // A condition is a hard programmed condition that decides if you can battle the Npc or not
    // An example is defeat all the other Npc's in a Gym before battling the Gym Leader. See Condition.groovy.
    Condition condition
    boolean triggerOnActionButton = true
    boolean triggerBeforeStep = false
    boolean placeOneTimeActionLock = false

    String identifier

    game.Map map // An action can either belong to an map or an altMap
    AltMap altMap
    int positionX
    int positionY

    String conditionMetMessage
    String conditionNotMetMessage

    boolean conditionalStep = false

    static mapping = {
        condition column: "`condition`" // Condition is a reserved keyword in mysql
    }

    static constraints = {
        identifier nullable:true
        image nullable: true
        correctionLeft nullable:true
        correctionTop nullable:true
        actionFunction nullable:true
        condition nullable: true
        conditionNotMetMessage nullable: true
        conditionMetMessage nullable: true
        cssClass nullable: true
        altMap nullable: true
        map nullable: true
    }

    def beforeInsert() {
        if (!identifier){
            identifier = RandomStringUtils.random(15, true, true)
        }
    }

    def beforeUpdate() {
        if (!identifier){
            identifier = RandomStringUtils.random(15, true, true)
        }
    }

}
