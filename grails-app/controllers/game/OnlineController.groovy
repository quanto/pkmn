package game

import game.context.PlayerData

class OnlineController {

    def sessionRegistry

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        render text: g.render(template: 'online', model: [players: Player.list(), ownPlayer: player, onlinePlayers: sessionRegistry.getAllPrincipals()])
    }
}
