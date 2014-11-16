package game

import game.context.PlayerData
import grails.converters.JSON

class StatsController {

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        def model = [
                pveBattlesWon: player.pveBattlesWon,
                pveBattlesLost: player.pveBattlesLost,
                pvnBattlesWon: player.pvnBattlesWon,
                pvnBattlesLost: player.pvnBattlesLost,
                pvpBattlesLost: player.pvpBattlesLost,
                pvpBattlesWon: player.pvpBattlesWon
        ]
        render model as JSON
    }
 \
}