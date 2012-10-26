package game.fight

import game.OwnerPokemon

class SwitchAction extends BattleAction {

    int playerNr
    OwnerPokemon ownerPokemon

    public SwitchAction(OwnerPokemon ownerPokemon, int playerNr){
        this.ownerPokemon = ownerPokemon
        this.playerNr = playerNr
    }
//        fight.log += "s:${playerNr}:" + ownerPokemon.pokemon.threeValueNumber() + ".gif;";
//        fight.log += "n:${playerNr}:" + ownerPokemon.pokemon.name + ";";
//        fight.log += "l:${playerNr}:" + ownerPokemon.level + ";";
}
