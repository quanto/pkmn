package game.fight.status

class Stat {

    public static int getStat(int stat, int stage)
    {
        double p
        if (stage == -6)
        {
            p = 0.25;
        }
        else if (stage == -5)
        {
            p = 0.285;
        }
        else if (stage == -4)
        {
            p = 0.33;
        }
        else if (stage == -3)
        {
            p = 0.4;
        }
        else if (stage == -2)
        {
            p = 0.5;
        }
        else if (stage == -1)
        {
            p = 0.66;
        }
        else if (stage == 0)
        {
            p = 1;
        }
        else if (stage == 1)
        {
            p = 1.5;
        }
        else if (stage == 2)
        {
            p = 2;
        }
        else if (stage == 3)
        {
            p = 2.5;
        }
        else if (stage == 4)
        {
            p = 3;
        }
        else if (stage == 5)
        {
            p = 3.5;
        }
        else if (stage == 6)
        {
            p = 4;
        }
        return Math.round(p * stat);
    }
    
}
