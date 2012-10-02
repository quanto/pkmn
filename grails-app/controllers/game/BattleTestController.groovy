package game

class BattleTestController {

    def index() {
        // Set test Owner
        Owner owner = Owner.findByName("Kevin")
        session.owner = owner
        // Set test fight
//        session.fight = Fight.findByPlayer1(session.owner)
        Pokemon wildPokemon = Pokemon.findByNr(1)
        new BattleFunctions().startFight(BattleType.PVE,owner,wildPokemon,1)

        render text:"Done ${session.owner}"
    }




}
