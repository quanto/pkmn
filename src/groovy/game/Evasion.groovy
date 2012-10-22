package game

class Evasion {

    public static void addToEvasionStage(int addToStage, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer)
    {

        if (addToStage > 0)
        {
            // Max 6
            if (attackingFightPlayer.evasionStage != 6)
            {
                attackingFightPlayer.evasionStage += addToStage;
                attackingFightPlayer.fight.log += "m:${attackingFightPlayer.ownerPokemon.pokemon.name}`s evasiveness raises.;";
            }
        }
        else
        {
            // min -6
            if (defendingFightPlayer.evasionStage != -6)
            {
                defendingFightPlayer.evasionStage += addToStage;
                attackingFightPlayer.fight.log += "m:${defendingFightPlayer.ownerPokemon.pokemon.name}`s evasiveness lowers.;";
            }
        }
    }

    public static double getEvasion(double evasion, int stage)
    {
        double p = 0
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
