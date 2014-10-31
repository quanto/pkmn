package game

import game.action.RecoverAction
import game.context.*
import game.fight.action.BattleAction;
import game.fight.action.MoveAction

class TestObjects {
	
	public static BattleAction getTackleBattleAction(){
		Move move = getTackleMove()
		BattleAction battleAction = new MoveAction(
			move: move
		)
		return battleAction
	}
	
	public static OwnerPokemon getOwnerPokemon(Pokemon playerPokemon, Owner owner, int partyPosition){
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
		ownerPokemon.ownerMoves = []
		ownerPokemon.ownerMoves.add(new OwnerMove(
			id: 1,
			move: getTackleMove(),
			ppLeft: 10
		))
		
		ownerPokemon.hp = ownerPokemon.calculateHP()
		ownerPokemon.owner = owner
		ownerPokemon.save()
		return ownerPokemon
	}
	
	public static Player getPlayer(){
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
	
	public static Move getTackleMove(){
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
	
	public static Pokemon getTestPokemon(){
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
