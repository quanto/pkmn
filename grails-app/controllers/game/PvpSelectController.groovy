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
            boolean haveInvite = fightInviteService.getInvite(player)

            if (haveInvite){
                render text: g.render(template: 'waiting')
            }
            else {
                render text: g.render(template: 'inviteOverview', model: [invites:fightInviteService.getInvites(), player:player])
            }
        }
    }

    def cancelInvite(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        fightInviteService.cancelInvite(player)
        redirect controller: 'game'
    }

    def inviteAccepted(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        render text: player.view == View.Battle
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

    def exit = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowPvpSelect){
            render text : "Fout"
        }
        else {
            fightInviteService.cancelInvite(player)

            player.view = View.ShowMap
            player.save()
            redirect controller: 'game', action:'index'
        }
    }

}
