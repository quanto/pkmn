package game

class BattleOrder {

    public static boolean player1First(Fight fight){

        Random random = new Random()

        boolean player1first

        // Bepaal wie begint
        if (Speed.isSpeedMove(fight.fightPlayer1))
        {
            if (!Speed.isSpeedMove(fight.fightPlayer2))
            {
                player1first = true;
            }
            else
            {
                if (fight.fightPlayer1.speed > fight.fightPlayer2.speed)
                    player1first = true;
                else if (fight.fightPlayer1.speed == fight.fightPlayer1.speed)
                {
                    if (random.nextInt(2) == 1)
                        player1first = true;
                    else
                        player1first = false;
                }
                else
                    player1first = false;
            }
        }
        else if (Speed.isSpeedMove(fight.fightPlayer2))
        {
            player1first = false;
        }
        else
        {
            if (fight.fightPlayer1.speed > fight.fightPlayer2.speed)
                player1first = true;
            else if (fight.fightPlayer1.speed == fight.fightPlayer2.speed)
            {
                if (random.nextInt(2) == 1)
                    player1first = true;
                else
                    player1first = false;
            }
            else
                player1first = false;
        }
        return player1first
    }

}
