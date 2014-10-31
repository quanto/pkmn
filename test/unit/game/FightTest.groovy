package game

import game.action.RecoverAction
import game.context.Fight
import game.context.FightPlayer


import org.junit.Test

import game.context.*
import game.fight.action.BattleAction
import game.fight.action.FailAction
import game.fight.action.MoveAction
import game.fight.action.SwitchAction
import grails.test.GrailsUnitTestCase
import grails.test.mixin.Mock
import game.action.*
import game.lock.*

@Mock([Owner, Player, OwnerPokemon, Pokemon, Action, RecoverAction, Map, NpcAction, Lock, NpcLock, Npc])
class FightTest {
	
	FightFactoryService fightFactoryService = new FightFactoryService()
	TestFightHelper testFightHelper = new TestFightHelper()
	
	@Before
	public void setUp(){
		testFightHelper.setUp()
	}
	
	@Test
	public void pveBattle(){
		
		Player player = TestObjects.getPlayer()
		Pokemon playerPokemon = TestObjects.getTestPokemon()
		OwnerPokemon ownerPokemon = TestObjects.getOwnerPokemon(playerPokemon, player, 1)
		Pokemon wildPokemon = TestObjects.getTestPokemon()
		
		testFightHelper.addownerPokemonLink(player, ownerPokemon)

		testFightHelper.setMetaClasses()
		
		Fight fight = fightFactoryService.startFight(BattleType.PVE, player,null,wildPokemon,1)
		fight.fightPlayer1.metaClass.getOwner = { -> return player }
		
		while (!fight.battleOver){
			Moves.setMove(fight, fight.fightPlayer1, TestObjects.getTackleBattleAction(), false)
			println "****"
			fight.roundResult.toBattleString(fight.fightPlayer1).split(";").each{
				println(it)
			}
		}
	}
	
	@Test
	public void pvnBattle(){
		
		Player player1 = TestObjects.getPlayer()
		NpcAction npcAction = new NpcAction(
			message: ""
		)
		Pokemon playerPokemon = TestObjects.getTestPokemon()
		OwnerPokemon playerOwnerPokemon1 = TestObjects.getOwnerPokemon(playerPokemon, player1, 1)
		testFightHelper.addownerPokemonLink(player1, playerOwnerPokemon1)
		
		Npc npc = new Npc(
			ownerId: 3,
			npcAction: npcAction,
			rewardItems: [],
			permanentLock: false,
			name: "testNpc"
		)
		npc.save(flush:true)
		
		Pokemon npcPokemon = TestObjects.getTestPokemon()
		OwnerPokemon npcOwnerPokemon = TestObjects.getOwnerPokemon(npcPokemon, npc, 1)
		testFightHelper.addownerPokemonLink(npc, npcOwnerPokemon)
		testFightHelper.setMetaClasses()
		
		Fight fight = fightFactoryService.startFight(BattleType.PVN, player1, npc, null, null)
		fight.fightPlayer1.metaClass.getOwner = { -> return player1 }
		fight.fightPlayer2.metaClass.getOwner = { -> return npc }
		
		while (!fight.battleOver){			
			Moves.setMove(fight, fight.fightPlayer1, TestObjects.getTackleBattleAction(), false)
			println "****"
			fight.roundResult.toBattleString(fight.fightPlayer1).split(";").each{
				println(it)
			}
		}
	}
	
	@Test
	public void pvnBattleMultiplePlayerPokemon(){
		
		Player player1 = TestObjects.getPlayer()
		NpcAction npcAction = new NpcAction(
			message: ""
		)
		Pokemon playerPokemon = TestObjects.getTestPokemon()
		OwnerPokemon playerOwnerPokemon1 = TestObjects.getOwnerPokemon(playerPokemon, player1, 1)
		OwnerPokemon playerOwnerPokemon2 = TestObjects.getOwnerPokemon(playerPokemon, player1, 2)
		testFightHelper.addownerPokemonLink(player1, playerOwnerPokemon1)
		testFightHelper.addownerPokemonLink(player1, playerOwnerPokemon2)
		
		Npc npc = new Npc(
			ownerId: 3,
			npcAction: npcAction,
			rewardItems: [],
			permanentLock: false,
			name: "testNpc"
		)
		npc.save(flush:true)
		
		Pokemon npcPokemon = TestObjects.getTestPokemon()
		OwnerPokemon npcOwnerPokemon1 = TestObjects.getOwnerPokemon(npcPokemon, npc, 1)
		OwnerPokemon npcOwnerPokemon2 = TestObjects.getOwnerPokemon(npcPokemon, npc, 2)
		testFightHelper.addownerPokemonLink(npc, npcOwnerPokemon1)
		//addownerPokemonLink(npc, npcOwnerPokemon2)
		
		testFightHelper.setMetaClasses()
		
		Fight fight = fightFactoryService.startFight(BattleType.PVN, player1, npc, null, null)
		fight.fightPlayer1.metaClass.getOwner = { -> return player1 }
		fight.fightPlayer2.metaClass.getOwner = { -> return npc }
		
		
		while (!fight.battleOver){
			if (fight.fightPlayer1.fightPokemon.hp > 0){
				Moves.setMove(fight, fight.fightPlayer1, new FailAction(), false)
			}
			else {
				FightPokemon fightPokemon = fight.fightPlayer1.party.find{ it.hp > 0 }
				assert fightPokemon
				assert fight.switchRound
				SwitchAction switchAction = new SwitchAction(fightPokemon: fightPokemon)
				Moves.setMove(fight, fight.fightPlayer1, switchAction, false)
			}
			
			println "****"
			fight.roundResult.toBattleString(fight.fightPlayer1).split(";").each{
				println(it)
			}
		}
	}
	
	
	
}
