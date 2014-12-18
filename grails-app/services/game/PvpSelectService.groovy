package game

class PvpSelectService {

    FightInviteService fightInviteService

    static transactional = false

    def getPvpSelectModel(Player player) {
        boolean haveInvite = fightInviteService.getInvite(player)

        if (haveInvite){
            return [
                    waiting: true
            ]
        }
        else {
            def model = [
                    invites: fightInviteService.getInvites().findAll{ it.player.id != player.id }.collect{[
                            inviteNr: it.inviteNr,
//                            player: {
//                                username: it.player.username
//                            },
                            dateCreated: it.dateCreated,
                    ]}
            ]
            return model
            //  render text: g.render(template: 'inviteOverview', model: [invites:fightInviteService.getInvites(), player:player])
        }
    }
}
