package game

import org.apache.commons.lang.RandomStringUtils

class Npc extends Owner {

    String identifier = RandomStringUtils.random(15, true, true)
    boolean permanentLock = false
    NpcAction npcAction

    static hasMany = [rewardItems: OwnerItem, ownerItems: OwnerItem]

    static constraints = {
        npcAction nullable: true
    }

}
