package game.fight

import game.fight.log.BattleLog
import game.fight.log.DingLog
import game.fight.log.MessageLog
import game.fight.log.MoveLog
import game.fight.log.SwitchLog

/**
 * Adapter between UI and battle engine
 */
class RoundResult {

    List<BattleLog> battleActions = []

    List<BattleLog> personalActions = []

    public String toBattleString(){
        if (personalActions){
            String battleString = convertBattleString(personalActions)
            personalActions = [] // Clear them after seeing them
            return battleString
        }
        else {
            String battleString = convertBattleString(battleActions)
            return battleString
        }
    }

    private String convertBattleString(List<BattleLog> actions){
        String actionString = ""

        actions.each{ BattleLog battleAction ->

            if (battleAction in MessageLog){
                actionString += "m:${battleAction.message};"
            }
            else if (battleAction in DingLog){
                actionString += "l:${battleAction.playerNr}:${battleAction.level};"
            }
            else if (battleAction in SwitchLog){
                actionString += "h:${battleAction.playerNr}:" + battleAction.ownerPokemon.hp + ";";
                actionString += "x:${battleAction.playerNr}:" + battleAction.ownerPokemon.calculateHP() + ";";
                actionString += "s:${battleAction.playerNr}:" + battleAction.ownerPokemon.pokemon.threeValueNumber() + ".gif;";
                actionString += "n:${battleAction.playerNr}:" + battleAction.ownerPokemon.pokemon.name + ";";
                actionString += "l:${battleAction.playerNr}:" + battleAction.ownerPokemon.level + ";";

            }
            else if (battleAction in MoveLog){
                actionString += "a:${battleAction.playerNr}:${battleAction.damage};";
            }
            // :TODO evolve
        }
        return actionString
    }

}
