package game.fight.status

import game.context.FightPlayer
import game.Move

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 20:20
 * To change this template use File | Settings | File Templates.
 */
class Speed {

    public static boolean isSpeedMove(FightPlayer fightPlayer)
    {
        Move move = fightPlayer.move

        if (!move){
            return false
        }
        //12 Aqua Jet 50 Bullet Punch 121 Extremespeed 198 Ice Shard 226 Mach Punch 306 Quick Attack 351 Shadow Sneak 442 Vacuum Wave
        if (move.id == 12 || move.id == 50 || move.id == 121 || move.id == 198 || move.id == 226 || move.id == 306 || move.id == 351 || move.id == 442)
        {
            return true;
        }
        else {
            return false
        }
    }

}
