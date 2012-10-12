package game

class FightTests extends GroovyTestCase {

    FightFactoryService fightFactoryService

    void testPVE() {
        Player player = Player.get(1)
        Pokemon pokemon = Pokemon.get(1)
        Move move = Move.findByName("Tackle")

        Fight fight = fightFactoryService.startFight(BattleType.PVE,player,null,pokemon,1)
        assertNotNull fight

        while (!fight.battleOver){
            Moves.setMove(fight,fight.fightPlayer1, move)
            println fight.log
        }
    }

    void testPVN() {
        Player player = Player.get(1)
        Owner npc = Owner.findByName("Npc1")
        Move move = Move.findByName("Tackle")

        Fight fight = fightFactoryService.startFight(BattleType.PVN,player,npc,null,null)
        assertNotNull fight

        while (!fight.battleOver && fight.fightPlayer1.ownerPokemon.hp > 0){  // Stop if the user has to chose another pokemon

            Moves.setMove(fight,fight.fightPlayer1, move)
            println fight.log
        }
    }

}
