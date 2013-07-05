package game.action

import org.apache.commons.lang.RandomStringUtils
import game.context.ActionType

class Action {

    ActionType actionType

    String actionFunction // The js function to call to perform the action
    String tileImage // Tile to show frontend


    // A condition is a hard programmed condition that decides if you can battle the Npc or not
    // An example is defeat all the other Npc's in a Gym before battling the Gym Leader. See Condition.groovy.
    String condition
    boolean triggerOnActionButton = true
    boolean triggerBeforeStep = false
    boolean placeOneTimeActionLock = false

    String identifier
    game.Map map
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
        tileImage nullable: true
        actionFunction nullable:true
        condition nullable: true
        conditionNotMetMessage nullable: true
        conditionMetMessage nullable: true
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
