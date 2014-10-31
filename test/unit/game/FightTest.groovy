package game

import game.action.RecoverAction
import game.context.Fight
import game.context.FightPlayer
import game.context.MoveCategory

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
	
	//TestFightHelper testFightHelper = new TestFightHelper()
	List<OwnerPokemonLink> ownerPokemonLinks
	
	@Before
	public void setUp(){
		ownerPokemonLinks = []
	}
	
	private class OwnerPokemonLink {
		Owner owner
		List<OwnerPokemon> ownerPokemon = []
	}
	
	private void addownerPokemonLink(Owner owner, OwnerPokemon ownerPokemon){
		OwnerPokemonLink ownerPokemonLink = ownerPokemonLinks.find{ it.owner.name == owner.name }
		if (!ownerPokemonLink){
			ownerPokemonLink = new OwnerPokemonLink()
			ownerPokemonLink.owner = owner
			ownerPokemonLinks.add(ownerPokemonLink)
		}
		ownerPokemonLink.ownerPokemon.add(ownerPokemon)
	}
	
	@Test
	public void pveBattle(){
		
		Player player = getPlayer()
		Pokemon playerPokemon = getTestPokemon()
		OwnerPokemon ownerPokemon = getOwnerPokemon(playerPokemon, player, 1)
		Pokemon wildPokemon = getTestPokemon()
		
		addownerPokemonLink(player, ownerPokemon)

		setMetaClasses()
		
		Fight fight = fightFactoryService.startFight(BattleType.PVE, player,null,wildPokemon,1)
		fight.fightPlayer1.metaClass.getOwner = { -> return player }
		
		while (!fight.battleOver){
			Moves.setMove(fight, fight.fightPlayer1, getTackleMove(), false)
			println "****"
			fight.roundResult.toBattleString(fight.fightPlayer1).split(";").each{
				println(it)
			}
		}
	}
	
	@Test
	public void pvnBattle(){
		
		Player player1 = getPlayer()
		NpcAction npcAction = new NpcAction(
			message: ""
		)
		Pokemon playerPokemon = getTestPokemon()
		OwnerPokemon playerOwnerPokemon1 = getOwnerPokemon(playerPokemon, player1, 1)
		addownerPokemonLink(player, playerOwnerPokemon1)
		
		Npc npc = new Npc(
			ownerId: 3,
			npcAction: npcAction,
			rewardItems: [],
			permanentLock: false,
			name: "testNpc"
		)
		npc.save(flush:true)
		
		Pokemon npcPokemon = getTestPokemon()
		OwnerPokemon npcOwnerPokemon = getOwnerPokemon(npcPokemon, npc, 1)
		addownerPokemonLink(npc, npcOwnerPokemon)
		setMetaClasses()
		
		Fight fight = fightFactoryService.startFight(BattleType.PVN, player1, npc, null, null)
		fight.fightPlayer1.metaClass.getOwner = { -> return player1 }
		fight.fightPlayer2.metaClass.getOwner = { -> return npc }
		
		
		while (!fight.battleOver){			
			Moves.setMove(fight, fight.fightPlayer1, getTackleMove(), false)
			println "****"
			fight.roundResult.toBattleString(fight.fightPlayer1).split(";").each{
				println(it)
			}
		}
	}
	
	@Test
	public void pvnBattleMultiplePokemon(){
		
		Player player1 = getPlayer()
		NpcAction npcAction = new NpcAction(
			message: ""
		)
		Pokemon playerPokemon = getTestPokemon()
		OwnerPokemon playerOwnerPokemon1 = getOwnerPokemon(playerPokemon, player1, 1)
		OwnerPokemon playerOwnerPokemon2 = getOwnerPokemon(playerPokemon, player1, 2)
		addownerPokemonLink(player, playerOwnerPokemon1)
		addownerPokemonLink(player, playerOwnerPokemon2)
		
		Npc npc = new Npc(
			ownerId: 3,
			npcAction: npcAction,
			rewardItems: [],
			permanentLock: false,
			name: "testNpc"
		)
		npc.save(flush:true)
		
		Pokemon npcPokemon = getTestPokemon()
		OwnerPokemon npcOwnerPokemon1 = getOwnerPokemon(npcPokemon, npc, 1)
		OwnerPokemon npcOwnerPokemon2 = getOwnerPokemon(npcPokemon, npc, 2)
		addownerPokemonLink(npc, npcOwnerPokemon1)
		//addownerPokemonLink(npc, npcOwnerPokemon2)
		
		setMetaClasses()
		
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
	
	private BattleAction getTackleMove(){
		Move move = getMove()
		BattleAction battleAction = new MoveAction(
			move: move
		)
		return battleAction
	}
	
	private OwnerPokemon getOwnerPokemon(Pokemon playerPokemon, Owner owner, int partyPosition){
		OwnerPokemon ownerPokemon = new OwnerPokemon(
			level: 1,
			id: 1,
			partyPosition: partyPosition,
			pokemon: playerPokemon,
			hpIV: 1,
			attackIV: 1,
			defenseIV: 1,
			spAttackIV: 1,
			spDefenseIV: 1,
			speedIV: 1,
			gender: Gender.Male,
			ownerId: 1
		)
		ownerPokemon.hp = ownerPokemon.calculateHP()
		ownerPokemon.owner = owner
		ownerPokemon.save()
		return ownerPokemon
	}
	
	private void setMetaClasses(){
		OwnerPokemon.metaClass.static.findAllByPartyPositionGreaterThanAndOwner = { int i, Owner owner -> 
			OwnerPokemonLink result = ownerPokemonLinks.find{ it.owner.name == owner.name }
			assert result
			return result.ownerPokemon
		}
		Party.metaClass.static.getFirstAlivePokemon = { Owner owner -> 
			OwnerPokemonLink result = ownerPokemonLinks.find{ it.owner.name == owner.name }
			assert result.ownerPokemon[0]
			return result.ownerPokemon[0] // Geef gewoon de eerste terug
		}
		LearnableMove.metaClass.static.findAllByPokemonAndLearnLevelLessThanEquals = { Pokemon pokemon, int level -> return [ new LearnableMove(move: move) ] }
		Effectiveness.metaClass.static.findByType1AndType2AndAttackType = { String pokemonType1, String pokemonType2, String attackType -> return new Effectiveness(effect: 1.0) }
		Evolution.metaClass.static.findByFromPokemonAndLevel = { Pokemon pokemon, int level -> return null}
		LearnableMove.metaClass.static.findAllByPokemonAndLearnLevel = { Pokemon pokemon, int level -> return [] }
		Move.metaClass.static.findByName = { String name -> return getMove() }
	}
	
	private Player getPlayer(){
		Map map = new Map(name: "test")
		
		Player player = new Player(
			ownerId: 1,
			username:"kevin",
			email:"verhoef.k@gmail.com",
			password: "???",
			name: "kevin",
			lastRecoverAction: new RecoverAction(map: map),
			map: map
		)
		player.metaClass.beforeUpdate = { -> }
		player.metaClass.isDirty = { String s -> return false}
		player.metaClass.encodePassword = {
			return
		}
		return player
	}
	
	private Move getMove(){
		return new Move(
			nr: 412,
			name: "Tackle",
			type: "normal",
			category: MoveCategory.PhysicalMove,
			power: 35,
			accuracy : 95,
			pp : 35,
			effect : "",
			effectProb : 0,
			implemented : 1,
			priority: 0
		)
	}
	
	private Pokemon getTestPokemon(){
		Pokemon pokemon = new Pokemon(
			nr: 1,
			name: "Bulbasaur",
			type1: "grass",
			type2: "poison",
			baseHp: 45,
			baseAttack: 49,
			baseDefense: 49,
			baseSpAttack: 65,
			baseSpDefense: 65,
			baseSpeed: 45,
			maleRate: 87.5,
			femaleRate: 12.5,
			catchRate: 45,
			baseEXP: 64,
			levelRate: "Medium-Slow",
			height: "2' 4\" (0.7m)",
			weight: "15.2 lbs. (6.9kg)",
		)
		return pokemon
	}
}
