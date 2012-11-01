package game

import game.context.FightInvite
import game.context.BattleType
import game.context.Fight
import map.View
import game.fight.status.Recover

class FightInviteService {

    FightFactoryService fightFactoryService
    static transactional = false
    private static List<FightInvite> fightInvites = new ArrayList<FightInvite>()
    int fightInviteCount = 0

    void createInvite(Player player){
        if (!fightInvites.find{ it.player.id == player.id  }){
            fightInviteCount += 1
            fightInvites.add(new FightInvite(player: player,inviteNr:fightInviteCount))
        }
    }

    void acceptInvite(int inviteNr, Player player2){
        FightInvite fightInvite = fightInvites.find{ it.inviteNr == inviteNr }
        if (fightInvite && fightInvite.player.id != player2.id){
            Player player1 = fightInvite.player


            // Remove the invite from our list
            fightInvites.remove(fightInvite)

            // Recover before pvp begins
            Recover.recoverParty(player1)
            Recover.recoverParty(player2)

            // Start the fight
            Fight fight = fightFactoryService.startFight(BattleType.PVP, player1, player2, null, null)

            Player.withTransaction {

                player1 = Player.get(player1.id)

                player1.fightNr = fight.nr
                player1.view = View.Battle

                player2.fightNr = fight.nr
                player2.view = View.Battle


                player1.save()
                player2.save()
            }
        }
    }

    void cancelInvite(Player player){
        FightInvite fightInvite = fightInvites.find{ it.player.id == player.id }
        if (fightInvite){
            fightInvites.remove(fightInvite)
        }
    }

    FightInvite getInvite(Player player){
        return fightInvites.find{ it.player.id == player.id }
    }

    List getInvites(){
        return fightInvites
    }
}
