package game.fight

import game.fight.log.MessageLog
import game.fight.status.Stats
import game.context.BattleType
import game.context.Fight
import game.Moves
import game.fight.action.NoAction

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */
class Run {

    public static void run(Fight fight)
    {
        //http://www.upokecenter.com/games/gs/guides/escape.html
        if (fight.battleType == BattleType.PVE)
        {
            Random random = new Random()

            int a = fight.fightPlayer1.speed
            int b = Math.round(fight.fightPlayer2.speed / 4) % 255
            int c = fight.escapeAttempts
            int x = (a * 32 / b) + (30 * c)

            // kijk weg rennen succes

            if (x > 255 || random.nextInt(255) <= x || b == 0)
            {
                fight.roundResult.battleActions.add(new MessageLog("You run away safely."))
                fight.battleOver = true
            }
            else
            {
                fight.escapeAttempts += 1
                fight.roundResult.battleActions.add(new MessageLog("You fail to run away."))
                Moves.setMove(fight,fight.fightPlayer1,new NoAction(),false)
            }

        }
        else
        {
            fight.roundResult.personalActions.add(new MessageLog("You can not run away from this battle."))
        }
    }

}
