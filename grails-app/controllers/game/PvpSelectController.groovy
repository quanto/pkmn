package game

import game.context.PlayerData
import map.View

class PvpSelectController {

    FightInviteService fightInviteService

    def cancelInvite(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowPvpSelect){
            render text : "Fout"
            return
        }

        fightInviteService.cancelInvite(player)
        redirect controller: 'game'
    }

    def inviteAccepted(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowPvpSelect){
            render text : "Fout"
            return
        }

        render text: player.view == View.Battle
    }

    def createInvite(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view != View.ShowPvpSelect){
            render text : "Fout"
            return
        }

        fightInviteService.createInvite(player)

        render text: ""
    }

    def acceptInvite(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        int inviteNr = Integer.parseInt(params.id)
        fightInviteService.acceptInvite(inviteNr,player)

        if (player.view != View.ShowPvpSelect){
            render text : "Fout"
            return
        }

        render text: ""
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
            render text : ""
        }
    }

}
