package game

class Condition {

    public static boolean conditionEval(Player player, String condition){
        if ("defeatAllOtherNpcOnCurrentMap"){

            def c = NpcLock.createCriteria()
            int lockCount = c.count() {
                npc {
                    npcAction {
                        eq("map",player.map)
                    }
                }
            }
            int npcCount = NpcAction.countByMap(player.map)

            if (lockCount >= npcCount -1){
                return true
            }
        }
        return false
    }

}
