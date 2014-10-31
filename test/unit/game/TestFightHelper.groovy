package game

import java.util.List;

class TestFightHelper {
	List<OwnerPokemonLink> ownerPokemonLinks
	
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
	
	public void setMetaClasses(){
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
		LearnableMove.metaClass.static.findAllByPokemonAndLearnLevelLessThanEquals = { Pokemon pokemon, int level -> return [ new LearnableMove(move: TestObjects.getTackleMove() ) ] }
		Effectiveness.metaClass.static.findByType1AndType2AndAttackType = { String pokemonType1, String pokemonType2, String attackType -> return new Effectiveness(effect: 1.0) }
		Evolution.metaClass.static.findByFromPokemonAndLevel = { Pokemon pokemon, int level -> return null}
		LearnableMove.metaClass.static.findAllByPokemonAndLearnLevel = { Pokemon pokemon, int level -> return [] }
		Move.metaClass.static.findByName = { String name -> return TestObjects.getTackleMove() }
	}
}
