package game.fight.calculation

import game.context.Fight
import game.fight.action.SwitchAction
import game.fight.action.NoAction
import game.fight.action.ItemAction
import game.fight.action.MoveAction

class BattleOrder {

    public static boolean player1First(Fight fight){

        Random random = new Random()

        boolean player1first

        // Switching has priority
        if (fight.fightPlayer1.battleAction in SwitchAction){
            return true
        }
        else if (fight.fightPlayer2.battleAction in SwitchAction){
            return false
        }

        // NoAction also has priority
        if (fight.fightPlayer1.battleAction in NoAction){
            return true
        }
        else if (fight.fightPlayer2.battleAction in NoAction){
            return false
        }

        // Item action priority
        if (fight.fightPlayer1.battleAction in ItemAction){
            return true
        }
        else if (fight.fightPlayer2.battleAction in ItemAction){
            return false
        }

        // Priority moves
        if (fight.fightPlayer1.battleAction in MoveAction && fight.fightPlayer2.battleAction in MoveAction){
            if (fight.fightPlayer1.battleAction.move.priority > fight.fightPlayer2.battleAction.move.priority){
                return true
            }
            else if (fight.fightPlayer1.battleAction.move.priority < fight.fightPlayer2.battleAction.move.priority){
                return true
            }
        }

        if (fight.fightPlayer1.speed > fight.fightPlayer2.speed){
            player1first = true
        }
        else if (fight.fightPlayer1.speed == fight.fightPlayer1.speed)
        {
            if (random.nextInt(2) == 1)
                player1first = true
            else{
                player1first = false
            }
        }
        else {
            player1first = false;
        }

        return player1first
    }

}
