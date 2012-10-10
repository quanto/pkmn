package game

class BattleTestController {

    FightFactoryService fightFactoryService

    def index() {
        // Set test Owner
        Owner owner = Owner.findByName("Kevin")
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        // Set test fight
//        session.fight = Fight.findByPlayer1(session.owner)
        Pokemon wildPokemon = Pokemon.findByNr(1)
        Fight fight = fightFactoryService.startFight(BattleType.PVE,owner,null,wildPokemon,1)
        owner.fightNr = fight.nr

        redirect controller : 'battle'
    }




}
