package game.fight

/**
 * Adapter between UI and battle engine
 */
class RoundResult {

    List<BattleAction> battleActions = []

    List<BattleAction> personalActions = []

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

    private String convertBattleString(List<BattleAction> actions){
        String actionString = ""

        actions.each{ BattleAction battleAction ->

            if (battleAction in MessageAction){
                actionString += "m:${battleAction.message};"
            }
            else if (battleAction in DingAction){
                actionString += "l:${battleAction.playerNr}:${battleAction.level};"
            }
            else if (battleAction in SwitchAction){
                actionString += "s:${battleAction.playerNr}:" + battleAction.ownerPokemon.pokemon.threeValueNumber() + ".gif;";
                actionString += "n:${battleAction.playerNr}:" + battleAction.ownerPokemon.pokemon.name + ";";
                actionString += "l:${battleAction.playerNr}:" + battleAction.ownerPokemon.level + ";";
            }
            else if (battleAction in MoveAction){
                actionString += "a:${battleAction.playerNr}:${battleAction.damage};";
            }

        }
        return actionString
    }

}
