package game

import org.apache.commons.lang.RandomStringUtils

class Npc extends Owner {

    String identifier = RandomStringUtils.random(15, true, true)
    boolean permanentLock = false
    NpcAction npcAction

    // A condition is a hard programmed condition that decides if you can battle the Npc or not
    // An example is defeat all the other Npc's in a Gym before battling the Gym Leader. See Condition.groovy.
    String condition

    static constraints = {
        condition nullable: true
        npcAction nullable: true
    }

}
