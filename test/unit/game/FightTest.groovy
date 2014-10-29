package game

import game.action.RecoverAction
import game.context.Fight
import game.context.FightPlayer
import game.context.MoveCategory

import org.junit.Test

import game.context.*
import game.fight.action.BattleAction
import game.fight.action.MoveAction
import grails.test.GrailsUnitTestCase
import grails.test.mixin.Mock
import game.action.*
import game.lock.*

@Mock([Owner, Player, OwnerPokemon, Pokemon, Action, RecoverAction, Map, NpcAction, Lock, NpcLock, Npc])
class FightTest {
	
	FightFactoryService fightFactoryService = new FightFactoryService()
	
	@Test
	public void pveBattle(){
		
		Player player = getPlayer()
		Pokemon playerPokemon = getTestPokemon()
		OwnerPokemon ownerPokemon = getOwnerPokemon(playerPokemon)
		Pokemon wildPokemon = getTestPokemon()
		
		setMetaClasses(ownerPokemon)
		
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
		Npc npc = new Npc(
			npcAction: npcAction,
			rewardItems: [],
			permanentLock: false,
			name: "testNpc"
		)
		npc.save(flush:true)
		
		Pokemon playerPokemon = getTestPokemon()
		OwnerPokemon ownerPokemon = getOwnerPokemon(playerPokemon)
		Pokemon wildPokemon = getTestPokemon()
		
		setMetaClasses(ownerPokemon)
		
		Fight fight = fightFactoryService.startFight(BattleType.PVN, player1, npc, wildPokemon, null)
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
	
	private BattleAction getTackleMove(){
		Move move = getMove()
		BattleAction battleAction = new MoveAction(
			move: move
		)
		return battleAction
	}
	
	private OwnerPokemon getOwnerPokemon(Pokemon playerPokemon){
		OwnerPokemon ownerPokemon = new OwnerPokemon(
			level: 1,
			id: 1,
			partyPosition: 1,
			pokemon: playerPokemon,
			hpIV: 1,
			attackIV: 1,
			defenseIV: 1,
			spAttackIV: 1,
			spDefenseIV: 1,
			speedIV: 1,
			gender: Gender.Male
		)
		ownerPokemon.hp = ownerPokemon.calculateHP()
		ownerPokemon.owner = player
		ownerPokemon.save()
		return ownerPokemon
	}
	
	private void setMetaClasses(OwnerPokemon ownerPokemon){
		OwnerPokemon.metaClass.static.findAllByPartyPositionGreaterThanAndOwner = { int i, Owner o -> [ ownerPokemon ] }
		Party.metaClass.static.getFirstAlivePokemon = { Owner owner -> return ownerPokemon }
		LearnableMove.metaClass.static.findAllByPokemonAndLearnLevelLessThanEquals = { Pokemon pokemon, int level -> return [ new LearnableMove(move: move) ] }
		Effectiveness.metaClass.static.findByType1AndType2AndAttackType = { String pokemonType1, String pokemonType2, String attackType -> return new Effectiveness(effect: 1.0) }
		Evolution.metaClass.static.findByFromPokemonAndLevel = { Pokemon pokemon, int level -> return null}
		LearnableMove.metaClass.static.findAllByPokemonAndLearnLevel = { Pokemon pokemon, int level -> return [] }
		Move.metaClass.static.findByName = { String name -> return getMove() }
	}
	
	private Player getPlayer(){
		Map map = new Map(name: "test")
		
		Player player = new Player(
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
