package game

import game.action.NpcAction
import game.item.Item
import game.lock.NpcLock

enum Condition {

    defeatAllOtherNpcOnCurrentMap,
    haveBeginnerBadge,
    haveBoulderBadge,
    haveEarthBadge,
    haveOldFactoryKey,
    haveUndergroundPassageKey,
    isAdmin,

    public static boolean conditionEval(Player player, Condition condition){
        // Npc conditions
        if (condition == condition.defeatAllOtherNpcOnCurrentMap){

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

        else if (condition == condition.haveBeginnerBadge){
            Item item = Item.findByName("Beginner Badge")
            if (OwnerItem.findByOwnerAndItem(player,item)){
                return true
            }
        }
        else if (condition == condition.haveBoulderBadge){
            Item item = Item.findByName("Boulder Badge")
            if (OwnerItem.findByOwnerAndItem(player,item)){
                return true
            }
        }
        else if (condition == condition.haveEarthBadge){
            Item item = Item.findByName("Boulder Badge")
            if (OwnerItem.findByOwnerAndItem(player,item)){
                return true
            }
        }


        else if (condition == condition.haveOldFactoryKey){
            Item item = Item.findByName("Old factory key")
            if (OwnerItem.findByOwnerAndItem(player,item)){
                return true
            }
        }
        else if (condition == condition.haveUndergroundPassageKey){
            Item item = Item.findByName("Underground passage key")
            if (OwnerItem.findByOwnerAndItem(player,item)){
                return true
            }
        }



        // Map transition action
        else if (condition == condition.isAdmin){
            if (player.getAuthorities().find{ Role role -> role.authority == "ROLE_ADMIN" }){
                return true
            }
        }
        return false
    }

}
