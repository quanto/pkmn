package game

import org.apache.commons.lang.RandomStringUtils

class Npc extends Owner {

    String identifier = RandomStringUtils.random(15, true, true)
    boolean permanentLock = false
    NpcAction npcAction

    static constraints = {
        npcAction nullable: true
    }

}
