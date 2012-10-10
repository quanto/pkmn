package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 10-10-12
 * Time: 14:41
 * To change this template use File | Settings | File Templates.
 */
class FightTests extends GroovyTestCase {

    FightFactoryService fightFactoryService

    void testSomething() {
        Player player = Player.get(1)
        Pokemon pokemon = Pokemon.get(1)


        Fight fight = fightFactoryService.startFight(BattleType.PVE,player,pokemon,1)
        assertNotNull fight
    }

}
