package game.fight

import game.fight.log.MessageLog
import game.fight.status.Recover
import game.fight.status.Evasion
import game.fight.calculation.Critical
import game.context.FightPlayer
import game.context.MoveInfo
import game.Move

import game.OwnerPokemon
import game.context.Fight

class StatusMove {

    public static void getMoveInfo(MoveInfo moveInfo, Move move, Fight fight, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer){
        Random random = new Random()

        // User recovers 50% of its max HP.
        if (move.name == "Slack Off" || move.name == "Recover" || move.name == "Softboiled" || move.name == "Milk Drink" || move.name == "Synthesis")
        {
            moveInfo.effectAction = true;
            moveInfo.recoverAction = true;
            moveInfo.recover = Math.round(attackingFightPlayer.maxHp /2)
        }
        // Poisons the opponent, if it hits.
        else if (move.name == "Poison Gas" || move.name == "Poisonpowder")
        {
            moveInfo.effectAction = true;
            moveInfo.poisonAction = true;
        }
        // Increases the user's Defense by one stage.
        else if (move.name == "Harden")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = 1;
        }
        // Decreases opponent's Attack by one stage. 
        else if (move.name == "Growl")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = -1;
        }
        // Decreases the opponent's Speed by two stages, if it hits. 
        else if (move.name == "Scary Face")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = -2;
        }
        // Decreases the opponent's Defense by two stages, if it hits. 
        else if (move.name == "Screech")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = -2;
        }
        // Increases the user's Special Attack by two stages. 
        else if (move.name == "Nasty Plot")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpAttackStage = 2;
        }
        // Decreases opponent's Special Defense by two stages. 
        else if (move.name == "Metal Sound")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = -2;
        }
        // Increases user's Special Attack by one stage. 
        else if (move.name == "Growth")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpAttackStage = 1;
        }
        // Increases user's Defense by two stages.
        else if (move.name == "Acid Armor")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = 2;
        }
        // Raises the user's Speed by two stages.
        else if (move.name == "Agility")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = 2;
        }
        // Raises user's Special Defense by two stages.
        else if (move.name == "Amnesia")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = 2;
        }
        // Raises user's Defense by two stages. 
        else if (move.name == "Barrier")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = 2;
        }
        // Increases user's Defense and Special Defense by one stage each. 
        else if (move.name == "Defend Order")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = 1;
        }
        // Increases user's Attack and Defense by one stage. 
        else if (move.name == "Bulk Up")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 1;
            moveInfo.addToDefenseStage = 1;
        }
        // Increases user's Special Attack and Special Defense by one stage. 
        else if (move.name == "Calm Mind")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpAttackStage = 1;
            moveInfo.addToSpDefenseStage = 1;
        }
        // Decreases opponent's Defense by one stage. 
        else if (move.name == "Leer")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = -1;
        }
        // Increases the user's Attack by two stages. 
        else if (move.name == "Swords Dance")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 2;
        }
        // Increases user's Attack by one stage. 
        else if (move.name == "Meditate")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 1;
        }
        // Decreases the opponent's Attack and Defense by one stage each. 
        else if (move.name == "Tickle")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = -1;
            moveInfo.addToDefenseStage = -1;
        }
        // Decreases opponent's Speed by one stage. 
        else if (move.name == "String Shot")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = -1;
        }
        // Lowers opponent's Attack by two stages. 
        else if (move.name == "Charm")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = -2;
        }
        // Increases the user's Speed by two stages. 
        else if (move.name == "Rock Polish")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = 2;
        }
        // Increase user's Defense and Special Defense by one stage. 
        else if (move.name == "Cosmic Power")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = 1;
            moveInfo.addToSpDefenseStage = 1;
        }
        // Opponent's Speed decreases by two stages. 
        else if (move.name == "Cotton Spore")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = -2;
        }
        // Decreases user's Special Attack by two stages. 
        else if (move.name == "Psycho Boost")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.selfStageAction = true;
            moveInfo.addToSpAttackStage = -2;
        }
        // Increases the user's Attack by one stage. 
        else if (move.name == "Howl")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 1;
        }
        //  Increases user's Special Attack by two stages. 
        else if (move.name == "Tail Glow")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpAttackStage = 2;
        }
        // Increases the user's Defense by one stage. 
        else if (move.name == "Withdraw")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = 1;
        }
        // Increases the user's Defense by two stages. 
        else if (move.name == "Iron Defense")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = 2;
        }
        // Decreases opponent's Defense by one stage. 
        else if (move.name == "Tail Whip")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = -1;
        }
        // Increases user's Attack and Speed by one stage each. 
        else if (move.name == "Dragon Dance")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 1;
            moveInfo.addToSpeedStage = 1;
        }
        // 29 Belly Drum status move User loses 50% of its max HP, but Attack raises to maximum. 
        else if (move.name == "Belly Drum")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 6;

            attackingFightPlayer.hp = Math.round(attackingFightPlayer.hp - (attackingFightPlayer.maxHp / 2))

            fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.ownerPokemon.pokemon.name} cut its own hp and maximized attack."))

        }
        // Increases the user's Attack by one stage. 
        else if (move.name == "Sharpen")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 1;
        }
        // Decreases opponent's Special Defense by two stages. 
        else if (move.name == "Fake Tears")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = -2;
        }
        // Decreases opponent't Attack by two stages. 
        else if (move.name == "Featherdance")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = -2;
        }
        // causes bad poison
        else if (move.name == "Toxic")
        {
            moveInfo.effectAction = true;
            moveInfo.badlypoisondAction = true;
        }
        // Randomly raises one of the user's stats by two stages.
        else if (move.name == "Acupressure")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            int r = random.nextInt(5)+1
            if (r == 1)
                moveInfo.addToDefenseStage = 2;
            else if (r == 2)
                moveInfo.addToSpDefenseStage = 2;
            else if (r == 3)
                moveInfo.addToSpAttackStage = 2;
            else if (r == 4)
                moveInfo.addToAttackStage = 2;
            else if (r == 5)
                moveInfo.addToSpeedStage = 2;
        }
        // Focus Energy status move Increases Critical-Hit ratio by one stage 
        else if (move.name == "Focus Energy")
        {
            Critical.addCriticalStage(1,attackingFightPlayer)
            fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.ownerPokemon.pokemon.name} takes a deep breath and focuses to raise the critical-hit ratio of its attacks."))

        }
        // Doesn't do ANYTHING. 
        else if (move.name == "Splash")
        {
            fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.ownerPokemon.pokemon.name} splashes."))
        }
        // Decreases opponent's Accuracy by one stage, if it hits.
        else if (move.name == "Flash" || move.name == "Kinesis" || move.name == "Sand-attack" || move.name == "Smokescreen")
        {
            moveInfo.addToAccuracyStage = -1;
            moveInfo.effectAction = true;
        }
        // Puts opponent to sleep, if it hits.
        else if (move.name == "Grasswhistle" || move.name == "Hypnosis" || move.name == "Lovely Kiss" || move.name == "Sing" || move.name == "Sleep Powder" || move.name == "Spore")
        {
            moveInfo.effectAction = true;
            moveInfo.sleepAction = true;
        }
        // Causes a burn, if it hits. 
        else if (move.name == "Will-o-wisp")
        {
            moveInfo.effectAction = true;
            moveInfo.burnAction = true;
        }
        else if (move.name == "Sweet Scent")
        {
            Evasion.addToEvasionStage(-1, attackingFightPlayer, defendingFightPlayer)
        }
        // Causes paralysis, if it hits. 
        else if (move.name == "Stun Spore" ||move.name == "Thunder Wave")
        {
            moveInfo.effectAction = true;
            moveInfo.paralysisAction = true;
        }

        //62  status move Confuses opponent  
        else if (move.name == "Confuse Ray" || move.name == "Supersonic" || move.name == "Sweet Kiss")
        {
            moveInfo.effectAction = true;
            moveInfo.confusionAction = true;
        }
        //140 Flatter status move Confuses opponent, but raises its Special Attack by two stages. 
        else if (move.name == "Flatter")
        {
            moveInfo.effectAction = true;
            moveInfo.confusionAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpAttackStage = 2;
            moveInfo.openentStageAction = true;
        }
        // 404 Swagger status move Opponent becomes confused, but its Attack is raised by 2 stages. 
        else if (move.name == "Swagger")
        {
            moveInfo.effectAction = true;
            moveInfo.confusionAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 2;
            moveInfo.openentStageAction = true;
        }
        // 418  status move All Pokemon on the field become confused. 
        else if (move.name == "Teeter Dance")
        {
            moveInfo.effectAction = true;
            moveInfo.confusionAction = true;
            moveInfo.effectActionOnBoth = true;
        }
        // 84 Defog status move Lowers opponent's Evasiveness by one stage 
        else if (move.name == "Defog")
        {
            Evasion.addToEvasionStage(-1, attackingFightPlayer, defendingFightPlayer)
        }
        //95 Double Team status move Increases user's Evasiveness by one stage. 
        else if (move.name == "Double Team" || move.name == "Minimize")
        {
            Evasion.addToEvasionStage(-1, attackingFightPlayer, defendingFightPlayer)
        }
        else if (move.name == "Aqua Ring")
        {
            moveInfo.effectAction = true;
            moveInfo.continueMove = true;
        }
        // Aromatherapy status move Cures all status problems in your party. 
        else if (move.name == "Aromatherapy")
        {
            Recover.removeAllStatusAfflictions(attackingFightPlayer)

            List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwnerAndPartyPositionGreaterThan(attackingFightPlayer.owner,0)

            ownerPokemonList.each { OwnerPokemon ownerPokemon ->
                ownerPokemon.burn = 0
                ownerPokemon.freeze = 0
                ownerPokemon.sleep = 0
                ownerPokemon.paralysis = 0
                ownerPokemon.poison = 0
                ownerPokemon.save()
            }
            fight.roundResult.battleActions.add(new MessageLog("All status problems in ${attackingFightPlayer.owner.name}`s party have been cured."))
        }
        
    }

}
