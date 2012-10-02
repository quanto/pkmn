package game

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
            int b = (fight.fightPlayer2.speed / 4) % 255
            int c = fight.escapeAttempts
            int x = (a * 32 / b) + (30 * c)

            // kijk weg rennen succes

            if (x > 255 || random.nextInt(255) <= x || b == 0)
            {
                Stats.saveStats(fight.fightPlayer1)
                fight.log = "m:You run away safely."
                fight.battleOver = 1
            }
            else
            {
                fight.escapeAttempts += 1
                fight.log = "m:You fail to run away."
                Moves.setMove(-1,false)
            }

        }
        else
        {
            fight.log = "m:You can not run away from this battle."
        }
    }

}
