package game.fight.calculation

import game.context.FightPlayer

class Critical {

    /**
     *  Functie voor het toevoegen van critical hit stage,
     * kan niet hoger worden dan 4 omdat.
     * Wordt opgetelt bij de standaard stage van 1.
     * Max critical stage is dus 5.
     */
    public static void addCriticalStage(int addToStage, FightPlayer fightPlayer)
    {

        if (fightPlayer.fightPokemon.criticalStage + addToStage < 4)
            fightPlayer.fightPokemon.criticalStage += addToStage
        else
        fightPlayer.fightPokemon.criticalStage = 4
    }

}
