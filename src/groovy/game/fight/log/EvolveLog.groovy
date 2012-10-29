package game.fight.log

import game.Pokemon
import game.fight.log.BattleLog

class EvolveLog extends BattleLog {

    Pokemon fromPokemon
    Pokemon toPokemon

    public EvolveLog(Pokemon fromPokemon, Pokemon toPokemon){
        this.fromPokemon = fromPokemon
        this.toPokemon = toPokemon
    }
    //fight.log += "n:${ownfightPlayer.playerNr}:" + evolution.toPokemon.name + ";";
    //fight.log += "s:${ownfightPlayer.playerNr}:${evolution.toPokemon.threeValueNumber()}.gif;";
}
