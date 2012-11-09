package game.fight.status

import game.fight.log.MessageLog
import game.context.Fight
import game.context.FightPlayer

class Stage {

    public static void modifyStage(Fight fight, String statName, int addToStage, boolean selfStageAction, boolean openentStageAction, boolean effectSucces, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer)
    {

        if ((addToStage > 0 || selfStageAction) && effectSucces && !openentStageAction)
        {
            // Controlleer de rand waarden
            if (attackFightPlayer.fightPokemon["${statName}Stage"] + addToStage > 5 || attackFightPlayer.fightPokemon["${statName}Stage"] + addToStage < -5)
            {
                // Alleen wanneer de rand waarden nog kunnen
                if (attackFightPlayer.fightPokemon["${statName}Stage"] + addToStage == 6)
                {
                    attackFightPlayer.fightPokemon["${statName}Stage"]  = 6;
                    if (!selfStageAction)
                        fight.roundResult.battleActions.add(new MessageLog("${attackFightPlayer.fightPokemon.name}`s ${statName} went up."))

                }
                else if ((attackFightPlayer.fightPokemon["${statName}Stage"]  + addToStage) == -6)
                {
                    attackFightPlayer.fightPokemon["${statName}Stage"]  = -6;
                }
                else
                {
                    if (!selfStageAction)
                        fight.roundResult.battleActions.add(new MessageLog("${attackFightPlayer.fightPokemon.name}`s ${statName} can`t go up anymore."))
                }
            }
            else
            {
                attackFightPlayer.fightPokemon["${statName}Stage"]  = attackFightPlayer.fightPokemon["${statName}Stage"]  + addToStage;

                if (!selfStageAction)
                    fight.roundResult.battleActions.add(new MessageLog("${attackFightPlayer.fightPokemon.name}`s ${statName} went up."))

            }
        }
        else if ((addToStage < 0 || openentStageAction) && effectSucces)
        {
            // Controlleer de rand waarden
            if (defendingFightPlayer.fightPokemon["${statName}Stage"] + addToStage < -5 || openentStageAction)
            {
                // Alleen wanneer de rand waarden nog kunnen
                if (addToStage > 0) // positieve actie op tegenstander
                {
                    if ((defendingFightPlayer.fightPokemon["${statName}Stage"] + addToStage) <= 6)
                    {
                        defendingFightPlayer.fightPokemon["${statName}Stage"] += addToStage;

                        fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.fightPokemon.name}`s ${statName} went up."))

                    }
                    else
                    {
                        defendingFightPlayer.fightPokemon["${statName}Stage"] = 6;
                    }
                }
                else if ((defendingFightPlayer.fightPokemon["${statName}Stage"] + addToStage) == -6)
                {
                    defendingFightPlayer.fightPokemon["${statName}Stage"] = -6;
                    fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.fightPokemon.name}`s ${statName} lowers."))

                }
                else
                {
                    fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.fightPokemon.name}`s ${statName} can`t lower anymore."))

                }
            }
            else
            {
                defendingFightPlayer.fightPokemon["${statName}Stage"] = defendingFightPlayer.fightPokemon["${statName}Stage"] + addToStage;
                fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.fightPokemon.name}`s ${statName} lowers."))

            }
        }
    }
    
}
