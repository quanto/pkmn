package game.fight.log

import game.OwnerPokemon

/**
 * We set this at the start of a round to ensure the current hp is correct.
 */
class InitialHpLog extends BattleLog {

    int hp
    int maxHp
    int playerNr
    String image
    String name
    int level

    public InitialHpLog(int hp, OwnerPokemon ownerPokemon, int playerNr){
        this.hp = hp
        this.playerNr = playerNr
        this.maxHp = ownerPokemon.calculateHP()
        this.image = ownerPokemon.pokemon.threeValueNumber() + ".gif"
        this.name = ownerPokemon.pokemon.name
        this.level = ownerPokemon.level
    }

}
