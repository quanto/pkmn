package game

class BattleTestController {

    FightFactoryService fightFactoryService

    def index() {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        // Set test fight
//        session.fight = Fight.findByPlayer1(session.owner)
        Pokemon wildPokemon = Pokemon.findByNr(1)
        Fight fight = fightFactoryService.startFight(BattleType.PVE,player,null,wildPokemon,1)
        player.fightNr = fight.nr

        redirect controller : 'battle'
    }




}
