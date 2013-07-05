package game

import game.action.NpcAction
import org.apache.commons.lang.RandomStringUtils

class Npc extends Owner {

    String identifier = RandomStringUtils.random(15, true, true)
    boolean permanentLock = false
    NpcAction npcAction
    // This message will be displayed if you try to start a battle but there's a lock in place
    String npcLockedMessage
    // This message will be displayed in battle after the npc is defeated
    String npcDefeatedMessage

    static hasMany = [rewardItems: OwnerItem, ownerItems: OwnerItem]

    static constraints = {
        npcAction nullable: true
        npcLockedMessage nullable: true
        npcDefeatedMessage nullable: true
    }

    static scaffoldList           = ['name','permanentLock']
    static scaffoldSearch         = ['name','permanentLock']

}
