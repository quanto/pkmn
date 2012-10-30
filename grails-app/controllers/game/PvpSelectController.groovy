package game

import game.context.PlayerData
import map.View

class PvpSelectController {

    FightInviteService fightInviteService

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowPvpSelect){
            render text : "Fout"
        }
        else {
            render text: g.render(template: 'inviteOverview', model: [invites:fightInviteService.getInvites()])
        }
    }

    def createInvite(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        fightInviteService.createInvite(player)

        redirect controller: "game"
    }

    def acceptInvite(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        int inviteNr = Integer.parseInt(params.id)
        fightInviteService.acceptInvite(inviteNr,player)

        redirect controller: "game"
    }
}
