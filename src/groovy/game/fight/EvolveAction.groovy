package game.fight

import game.Pokemon

class EvolveAction extends BattleAction {

    Pokemon fromPokemon
    Pokemon toPokemon

    public EvolveAction(Pokemon fromPokemon, Pokemon toPokemon){
        this.fromPokemon = fromPokemon
        this.toPokemon = toPokemon
    }
    //fight.log += "n:${ownfightPlayer.playerNr}:" + evolution.toPokemon.name + ";";
    //fight.log += "s:${ownfightPlayer.playerNr}:${evolution.toPokemon.threeValueNumber()}.gif;";
}
