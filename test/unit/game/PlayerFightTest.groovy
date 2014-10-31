package game;

import game.context.Fight;
import grails.test.mixin.Mock;

import game.action.RecoverAction
import game.context.Fight
import game.context.FightPlayer
import org.junit.Test
import game.context.*
import game.fight.action.BattleAction
import game.fight.action.FailAction
import game.fight.action.MoveAction
import game.fight.action.NoAction
import game.fight.action.SwitchAction
import grails.test.GrailsUnitTestCase
import grails.test.mixin.Mock
import game.action.*
import game.lock.*

@Mock([Owner, Player, OwnerPokemon, Pokemon, Action, RecoverAction, Map, NpcAction, Lock, NpcLock, Npc, OwnerMove])
public class PlayerFightTest {

	FightFactoryService fightFactoryService = new FightFactoryService()
	TestFightHelper testFightHelper = new TestFightHelper()
	
	@Before
	public void setUp(){
		testFightHelper.setUp()
	}
	
	@Test
	public void setPlayerMove(){
		
		Player player = TestObjects.getPlayer()
		Pokemon playerPokemon = TestObjects.getTestPokemon()
		OwnerPokemon ownerPokemon = TestObjects.getOwnerPokemon(playerPokemon, player, 1)
		Pokemon wildPokemon = TestObjects.getTestPokemon()
		
		testFightHelper.addownerPokemonLink(player, ownerPokemon)

		testFightHelper.setMetaClasses()
		
		Fight fight = fightFactoryService.startFight(BattleType.PVE, player, null, wildPokemon,1)
		fight.fightPlayer1.metaClass.getOwner = { -> return player }
		
		while (!fight.battleOver){
			
			// Zet een move met ownerMove id 1
			Moves.setPlayerMove(fight, fight.fightPlayer1, 1)
			
			println "****"
			fight.roundResult.toBattleString(fight.fightPlayer1).split(";").each{
				println(it)
			}
		}
	}
	
}