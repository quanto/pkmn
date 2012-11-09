package game.fight.log

import game.OwnerPokemon
import game.context.FightPokemon

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

    public InitialHpLog(FightPokemon fightPokemon, int playerNr){
        this.hp = fightPokemon.hp
        this.playerNr = playerNr
        this.maxHp = fightPokemon.maxHp
        this.image = fightPokemon.threeValueNumber() + ".gif"
        this.name = fightPokemon.name
        this.level = fightPokemon.level
    }

}
