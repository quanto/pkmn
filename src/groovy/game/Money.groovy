package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 09:06
 * To change this template use File | Settings | File Templates.
 */
class Money {

    public static void giveMoney(Fight fight, FightPlayer fightPlayer, int baseXp, int level, boolean isWild)
    {
        int money = baseXp * Math.ceil(level / 2) / 15;
        if (isWild)
            money = money / 3;

        money = Math.floor(money);

        if (money > 0)
        {
            fight.log += "m:You gained money money.;"
            Player player = fightPlayer.owner
            player.money += money
        }
    }

}
