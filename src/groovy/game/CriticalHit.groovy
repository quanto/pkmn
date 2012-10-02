package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 30-09-12
 * Time: 18:02
 * To change this template use File | Settings | File Templates.
 */
class CriticalHit {

    /*
      $stage is standaard altijd 1, sommige aanvallen brengen de stage omhoog.
      Met items kan de stage level verder omhoog gebracht worden of met de move Focus Energy.
    */
    public static boolean tryCriticalHit(int stage)
    {
        Random random = new Random()
        int r = random.nextInt(10000);
        double p

        if (stage == 1)
        {
            p = 6.25;
        }
        else if (stage == 2)
        {
            p = 12.5;
        }
        else if (stage == 3)
        {
            p = 25;
        }
        else if (stage == 4)
        {
            p = 33.5;
        }
        else
        {
            p = 50;
        }
        if (r < (p * 100))
            return true;
        else
            return false;

    }

}
