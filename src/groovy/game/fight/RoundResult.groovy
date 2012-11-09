package game.fight

import game.fight.log.BattleLog
import game.fight.log.DingLog
import game.fight.log.MessageLog
import game.fight.log.MoveLog
import game.fight.log.InitialHpLog
import game.context.FightPlayer

/**
 * Adapter between UI and battle engine
 */
class RoundResult {

    List<BattleLog> initialActions = []

    List<BattleLog> battleActions = []

    List<BattleLog> personalActions = []

    public String toBattleString(FightPlayer fightPlayer){

        // If where waiting we just want the initial string
        if (fightPlayer.waitOnOpponentMove){
            return convertBattleString(initialActions)
        }

        if (personalActions){
            String battleString = convertBattleString(initialActions) + convertBattleString(personalActions)
            personalActions = [] // Clear them after seeing them
            return battleString
        }
        else {
            String battleString = convertBattleString(initialActions) + convertBattleString(battleActions)
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
            else if (battleAction in MoveLog){
                actionString += "a:${battleAction.playerNr}:${battleAction.hp};";
            }
            else if (battleAction in InitialHpLog){
                actionString += "h:${battleAction.playerNr}:" + battleAction.hp + ";";
                actionString += "x:${battleAction.playerNr}:" + battleAction.maxHp + ";";
                actionString += "s:${battleAction.playerNr}:" + battleAction.image + ";"
                actionString += "n:${battleAction.playerNr}:" + battleAction.name + ";";
                actionString += "l:${battleAction.playerNr}:" + battleAction.level + ";";
            }

        }
        return actionString
    }

}
