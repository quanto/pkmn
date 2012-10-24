package game

//test
class Action {

    Map map
    int positionX
    int positionY

    // A condition is a hard programmed condition that decides if you can battle the Npc or not
    // An example is defeat all the other Npc's in a Gym before battling the Gym Leader. See Condition.groovy.
    String condition
    String conditionMetMessage
    String conditionNotMetMessage

    static constraints = {
        condition nullable: true
        conditionNotMetMessage nullable: true
        conditionMetMessage nullable: true
    }
}
