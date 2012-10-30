package game

import game.context.FightInvite

class FightInviteService {

    static transactional = false
    private static List<FightInvite> fightInvites = new ArrayList<FightInvite>()
    int fightInviteCount = 0

    void createInvite(Player player){
        if (!fightInvites.find{ it.player.id == player.id  }){
            fightInvites.add(new FightInvite(player: player))
        }
    }

    def List getInvites(){
        return fightInvites
    }
}
