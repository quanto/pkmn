package game

class StatsController {

    def index() {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        render text: g.render(template: 'stats', model: [player:player])
    }
}
