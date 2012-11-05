package game.fight

import game.context.FightPlayer
import game.Moves
import game.fight.action.MoveAction
import game.context.Fight

class ContinueMove {

    public static boolean continueMove(Fight fight, FightPlayer myFightPlayer){
        if (myFightPlayer.prepareMoveAction){
            // Continue the move that was prepared
            println "continue move"
            Moves.setMove(fight,myFightPlayer, myFightPlayer.prepareMoveAction)
            return true
        }
        return false
    }

}
