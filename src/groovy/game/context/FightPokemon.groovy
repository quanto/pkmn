package game.context

import game.OwnerPokemon

class FightPokemon {

    String name
    int ownerPokemonId
    int hp
    int attack
    int defense
    int spAttack
    int spDefense
    int speed
    int maxHp
    int level
    int burn
    int freeze
    int paralysis
    int poison
    int badlypoisond
    int sleep
    int confusion
    int curse
    int attackStage
    int defenseStage
    int spAttackStage
    int spDefenseStage
    int speedStage
    int accuracyStage
    int criticalStage
    int evasionStage
    String type1
    String type2
    int pokemonNr
    Gender gender
    int partyPosition

    /**
     * When making use of this object
     * we merge to attatch the object
     * to the session again.
     * @return
     */
    public OwnerPokemon getOwnerPokemon(){
        return OwnerPokemon.get(ownerPokemonId)
    }

    boolean hasType(String type){
        return type1 == type || type2 == type
    }

    String threeValueNumber()
    {
        return String.format("%3s", pokemonNr.toString()).replaceAll(' ','0')
    }

}
