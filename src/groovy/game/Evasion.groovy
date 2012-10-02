package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 30-09-12
 * Time: 09:20
 * To change this template use File | Settings | File Templates.
 */
class Evasion {

    public static int getEvasion(int evasion, int stage)
    {
        int p = 0
        if (stage == -6)
        {
            p = 3;
        }
        else if (stage == -5)
        {
            p = 2.66;
        }
        else if (stage == -4)
        {
            p = 2.33;
        }
        else if (stage == -3)
        {
            p = 2;
        }
        else if (stage == -2)
        {
            p = 1.66;
        }
        else if (stage == -1)
        {
            p = 1.33;
        }
        else if (stage == 0)
        {
            p = 1;
        }
        else if (stage == 1)
        {
            p = 0.75;
        }
        else if (stage == 2)
        {
            p = 0.6;
        }
        else if (stage == 3)
        {
            p = 0.5;
        }
        else if (stage == 4)
        {
            p = 0.428;
        }
        else if (stage == 5)
        {
            p = 0.375;
        }
        else if (stage == 6)
        {
            p = 0.33;
        }
        return p * evasion;
    }
    
}
