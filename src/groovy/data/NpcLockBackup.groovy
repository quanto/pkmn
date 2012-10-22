package data

import game.NpcLock
import game.Player

class NpcLockBackup {

    public static String getNpcLockData(Player player){
        String npcLockData = ""

        List<NpcLock> locks = NpcLock.findAllByPlayer(player)
        locks.each { NpcLock npcLock ->
            npcLockData += """<npcLock>
${npcLock.npc.identifier}
${npcLock.dateCreated.format("dd-MM-yyyy HH:mm:ss")}
${npcLock.permanent}
</npcLock>
            """
        }
        return npcLockData
    }

}
