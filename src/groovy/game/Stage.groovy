package game

import game.fight.MessageAction

class Stage {

    public static void modifyStage(Fight fight, String statName, int addToStage, boolean selfStageAction, boolean openentStageAction, boolean effectSucces, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer)
    {

        if ((addToStage > 0 || selfStageAction) && effectSucces && !openentStageAction)
        {
            // Controlleer de rand waarden
            if (attackFightPlayer["${statName}Stage"] + addToStage > 5 || attackFightPlayer["${statName}Stage"] + addToStage < -5)
            {
                // Alleen wanneer de rand waarden nog kunnen
                if (attackFightPlayer["${statName}Stage"] + addToStage == 6)
                {
                    attackFightPlayer["${statName}Stage"]  = 6;
                    if (!selfStageAction)
                        fight.roundResult.battleActions.add(new MessageAction("${attackFightPlayer.ownerPokemon.pokemon.name}`s ${statName} went up."))

                }
                else if ((attackFightPlayer["${statName}Stage"]  + addToStage) == -6)
                {
                    attackFightPlayer["${statName}Stage"]  = -6;
                }
                else
                {
                    if (!selfStageAction)
                        fight.roundResult.battleActions.add(new MessageAction("${attackFightPlayer.ownerPokemon.pokemon.name}`s ${statName} can`t go up anymore."))
                }
            }
            else
            {
                attackFightPlayer["${statName}Stage"]  = attackFightPlayer["${statName}Stage"]  + addToStage;

                if (!selfStageAction)
                    fight.roundResult.battleActions.add(new MessageAction("${attackFightPlayer.ownerPokemon.pokemon.name}`s ${statName} went up."))

            }
        }
        else if ((addToStage < 0 || openentStageAction) && effectSucces)
        {
            // Controlleer de rand waarden
            if (defendingFightPlayer["${statName}Stage"] + addToStage < -5 || openentStageAction)
            {
                // Alleen wanneer de rand waarden nog kunnen
                if (addToStage > 0) // positieve actie op tegenstander
                {
                    if ((defendingFightPlayer["${statName}Stage"] + addToStage) <= 6)
                    {
                        defendingFightPlayer["${statName}Stage"] += addToStage;

                        fight.roundResult.battleActions.add(new MessageAction("${defendingFightPlayer.ownerPokemon.pokemon.name}`s ${statName} went up."))

                    }
                    else
                    {
                        defendingFightPlayer["${statName}Stage"] = 6;
                    }
                }
                else if ((defendingFightPlayer["${statName}Stage"] + addToStage) == -6)
                {
                    defendingFightPlayer["${statName}Stage"] = -6;
                    fight.roundResult.battleActions.add(new MessageAction("${defendingFightPlayer.ownerPokemon.pokemon.name}`s ${statName} lowers."))

                }
                else
                {
                    fight.roundResult.battleActions.add(new MessageAction("${defendingFightPlayer.ownerPokemon.pokemon.name}`s ${statName} can`t lower anymore."))

                }
            }
            else
            {
                defendingFightPlayer["${statName}Stage"] = defendingFightPlayer["${statName}Stage"] + addToStage;
                fight.roundResult.battleActions.add(new MessageAction("${defendingFightPlayer.ownerPokemon.pokemon.name}`s ${statName} lowers."))

            }
        }
    }
    
}
