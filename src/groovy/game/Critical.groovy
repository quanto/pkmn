package game

class Critical {

    /**
     *  Functie voor het toevoegen van critical hit stage,
     * kan niet hoger worden dan 4 omdat.
     * Wordt opgetelt bij de standaard stage van 1.
     * Max critical stage is dus 5.
     */
    public static void addCriticalStage(int addToStage, FightPlayer fightPlayer)
    {

        if (fightPlayer.criticalStage + addToStage < 4)
            fightPlayer.criticalStage += addToStage
        else
        fightPlayer.criticalStage = 4
    }

}
