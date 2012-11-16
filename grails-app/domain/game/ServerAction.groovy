package game

/**
 * Actions that should be performed server side.
 */
class ServerAction extends Action {

    // A condition is a hard programmed condition that decides if you can battle the Npc or not
    // An example is defeat all the other Npc's in a Gym before battling the Gym Leader. See Condition.groovy.
    String condition
    String conditionMetMessage
    String conditionNotMetMessage

    boolean triggerOnActionButton = true
    boolean triggerBeforeStep = false
    boolean conditionalStep = false

    static mapping = {
        condition column: "`condition`" // Condition is a reserved keyword in mysql
    }

    static constraints = {
        condition nullable: true
        conditionNotMetMessage nullable: true
        conditionMetMessage nullable: true
    }

}
