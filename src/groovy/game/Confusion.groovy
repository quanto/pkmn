package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 08:57
 * To change this template use File | Settings | File Templates.
 */
class Confusion {

    public static void checkConfusion(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.confusion > 0)
        {
            // Geef bericht
            if (fightPlayer.confusion == 1)
            {
                fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} is no longer confused.;";
                fightPlayer.confusion = 0
//                if (player != 2 || fight.battleType != "pve")
//                {
                    // :TODO what to do
//                    fight.update();
//                    header("Location: index.php");
//                }
            }
            else
            {
                Random r = new Random()
                // 50 % kans dat geen aanval gedaan kan worden
                if (r.nextInt(2) == 1)
                {
                    fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} is confused.;";

                    // verlaag beurten
                    fightPlayer.confusion -= 1;
                    // geen move
                    Moves.setMove(fight,fightPlayer,null)
                }
            }
        }
    }

}
