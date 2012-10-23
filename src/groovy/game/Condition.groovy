package game

class Condition {

    public static boolean conditionEval(Player player, String condition){
        // Npc conditions
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

        // Map transition action
        else if ("isAdmin"){
            if (player.getAuthorities().find{ Role role -> role.authority == "ROLE_ADMIN" }){
                return true
            }
        }

        return false
    }

}
