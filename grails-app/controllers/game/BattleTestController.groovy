package game

class BattleTestController {

    FightFactoryService fightFactoryService

    def index() {
        // Set test Owner
        Owner owner = Owner.findByName("Kevin")
        session.owner = owner
        // Set test fight
//        session.fight = Fight.findByPlayer1(session.owner)
        Pokemon wildPokemon = Pokemon.findByNr(1)
        Fight fight = fightFactoryService.startFight(BattleType.PVE,owner,wildPokemon,1)
        owner.fightNr = fight.nr

        redirect controller : 'battle'
    }




}
