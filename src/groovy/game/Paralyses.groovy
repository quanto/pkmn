package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 08:43
 * To change this template use File | Settings | File Templates.
 */
class Paralyses {

    public static void checkParalyses(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.paralysis == 1)
        {
            Random r = new Random();
            // Kijk of effected door paralysis
            if (r.nextInt(4)+1 == 2)
            {
                fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} is paralyzed. It can`t move.;";
                // geen move
                Moves.setMove(fight,fightPlayer,-1)
            }
        }
    }

}
