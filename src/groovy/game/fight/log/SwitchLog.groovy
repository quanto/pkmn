package game.fight.log

import game.OwnerPokemon
import game.fight.log.BattleLog

class SwitchLog extends BattleLog {

    int playerNr
    OwnerPokemon ownerPokemon

    public SwitchLog(OwnerPokemon ownerPokemon, int playerNr){
        this.ownerPokemon = ownerPokemon
        this.playerNr = playerNr
    }

}
