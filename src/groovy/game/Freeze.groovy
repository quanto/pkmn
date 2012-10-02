package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 20:14
 * To change this template use File | Settings | File Templates.
 */
class Freeze {

    public static void checkFreeze(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.freeze == 1)
        {
            Random r = new Random();
            // Kijk of freeze ophoud
            if (r.nextInt(10)+1 == 1)
            {

                fightPlayer.fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} is no longer frozen.;";
                fightPlayer.freeze = 0;
            }
            else
            {
                fightPlayer.fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} is frozen solid!;";

                Moves.setMove(fight,fightPlayer,-1)
            }
        }
    }

}
