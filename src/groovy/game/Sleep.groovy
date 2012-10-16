package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 09:04
 * To change this template use File | Settings | File Templates.
 */
class Sleep {

    public static void checkSleep(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.sleep > 0)
        {
            // Geef bericht
            if (fightPlayer.sleep == 1)
            {
                fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} wakes up.;"
                fightPlayer.sleep = 0
            }
            else
            {
                fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} is a sleep.;"

                // verlaag slaap beurten
                fightPlayer.sleep -= 1

                // geen move
                Moves.setMove(fight,fightPlayer,null)
            }
        }
    }
    
}
