package game.fight

import game.fight.log.MessageLog
import game.fight.calculation.Critical
import game.context.FightPlayer
import game.context.MoveInfo
import game.context.Fight
import game.Move
import game.OwnerMove
import game.fight.action.MoveAction

class FightMove {
    
    public static void getMoveInfo(MoveInfo moveInfo, Move move, Fight fight, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer){

        Random random = new Random()

        // Hits 2-5 times.
        if (move.name == "Arm Thrust" || move.name == "Bone Rush" || move.name == "Bullet Seed" || move.name == "Barrage" || move.name == "Pin Missile" || move.name == "Comet Punch" || move.name == "Icicle Spear" || move.name == "Rock Blast" || move.name == "Doubleslap" || move.name == "Fury Attack" || move.name == "Fury Swipes" || move.name == "Spike Cannon")
        {
            moveInfo.loop = random.nextInt(3) + 2
        }
        // Hits twice.
        else if (move.name == "Double Hit" || move.name == "Double Kick")
        {
            moveInfo.loop = 2
        }
        // Always inflicts 20 HP.
        else if (move.name == "Sonicboom")
        {
            moveInfo.calculateDamage = false
            moveInfo.damage = 20
        }

        // Always inflicts 40 HP. 
        else if (move.name == "Dragon Rage")
        {
            moveInfo.calculateDamage = false
            moveInfo.damage = 40
        }

        // Damage = User's level.
        else if (move.name == "Night Shade" || move.name == "Seismic Toss")
        {
            moveInfo.calculateDamage = false
            moveInfo.damage = attackingFightPlayer.level
        }

        // Damage = 1x to 1.5x user's level.
        else if (move.name == "Psywave")
        {
            moveInfo.calculateDamage = false
            double multiplyer = (random.nextInt(6) / 10) + 1
            moveInfo.damage = Math.round(attackingFightPlayer.level * multiplyer)
        }


        // Move power is random. Magnitude 4 = Power: 10 Magnitude 5 = Power: 30 Magnitude 6 = Power: 50 Magnitude 7 = Power: 70 Magnitude 8 = Power: 90 Magnitude 9 = Power: 110 Magnitude 10 = Power: 150
        else if (move.name == "Magnitude")
        {
            int rand = random.nextInt(7) + 4
            moveInfo.defaultAttackPower = false
            if (rand == 4)
                moveInfo.attackPower = 10
            else if (rand == 5)
                moveInfo.attackPower = 30
            else if (rand == 6)
                moveInfo.attackPower = 50
            else if (rand == 7)
                moveInfo.attackPower = 70
            else if (rand == 8)
                moveInfo.attackPower = 90
            else if (rand == 9)
                moveInfo.attackPower = 110
            else if (rand == 10)
                moveInfo.attackPower = 150

            fight.roundResult.battleActions.add(new MessageLog("Magnitude level ${rand}."))
        }
        // May cause poison.
        else if (move.name == "Gunk Shot" || move.name == "Poison Jab" || move.name == "Poison Sting" || move.name == "Sludge" || move.name == "Sludge Bomb" || move.name == "Smog")
        {
            moveInfo.effectAction = true
            moveInfo.poisonAction = true
        }
        // May cause a burn.
        else if (move.name == "Sacred Fire" || move.name == "Ember" || move.name == "Fire Blast" || move.name == "Fire Punch" || move.name == "Flame Wheel" || move.name == "Flamethrower" || move.name == "Heat Wave" || move.name == "Lava Plume") //
        {
            moveInfo.effectAction = true;
            moveInfo.burnAction = true;
        }
        // Causes paralysis, if it hits.
        else if (move.name == "Glare" || move.name == "Zap Cannon")
        {
            moveInfo.effectAction = true;
            moveInfo.paralysisAction = true;
        }
        // May cause paralysis.
        else if (move.name == "Body Slam" || move.name == "Discharge" || move.name == "Dragonbreath" || move.name == "Force Palm" || move.name == "Lick" || move.name == "Spark" || move.name == "Thunder" || move.name == "Thunderbolt" || move.name == "Thunderpunch" || move.name == "Thundershock")//
        {
            moveInfo.effectAction = true;
            moveInfo.paralysisAction = true;
        }
        // May cause paralysis and flinching.
        else if (move.name == "Thunder Fang")
        {
            moveInfo.effectAction = true;
            moveInfo.paralysisAction = true;
            moveInfo.flinchAction = true;
        }
        // May cause poison that gets worse each turn.
        else if (move.name == "Poison Fang")
        {
            moveInfo.effectAction = true;
            moveInfo.badlypoisondAction = true;
        }
        else if (move.name == "Twineedle")
        {
            moveInfo.loop = 2;
            moveInfo.effectAction = true;
            moveInfo.poisonAction = true;
        }
        // May lower opponent's Defense or Special Defense one stage.
        else if (move.name == "Acid")
        {
            moveInfo.effectAction = true
            moveInfo.stageAction = true

            if (random.nextInt(2)  == 1)
                moveInfo.addToDefenseStage = -1;
            else
                moveInfo.addToSpDefenseStage = -1;
        }
        // 130  physical move May cause a burn or flinching.
        else if (move.name == "Fire Fang")
        {
            moveInfo.effectAction = true;

            if (random.nextInt(2) == 1)
                moveInfo.burnAction = true;
            else
                moveInfo.flinchAction = true;
        }
        // Might lower opponent's Attack.
        else if (move.name == "Aurora Beam")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = -1;
        }
        // Decreases the user's Speed by one stage.
        else if (move.name == "Hammer Arm")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.selfStageAction = true;
            moveInfo.addToSpeedStage = -1;
        }
        // May lower opponent's Speed by one stage.
        else if (move.name == "Bubble" || move.name == "Bubblebeam")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = -1;
        }
        // May lower opponent's Special Defense by one stage.
        else if (move.name == "Bug Buzz")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = -1;
        }
        // Might raise all of the user's stats by one stage.
        // May increase all of the user's stats by one stage each.
        else if (move.name == "Ancientpower" || move.name == "Silver Wind" || move.name == "Ominous Wind")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = 1;
            moveInfo.addToDefenseStage = 1;
            moveInfo.addToAttackStage = 1;
            moveInfo.addToSpDefenseStage = 1;
            moveInfo.addToSpAttackStage = 1;
        }
        // May decrease opponent's Defense by one stage.
        else if (move.name == "Crunch")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = -1;
        }
        // Likely to lower opponent's Special Attack by one stage.
        else if (move.name == "Charge Beam")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpAttackStage = -1;
        }
        // May decreases the opponent's Special Defense by one stage.
        else if (move.name == "Psychic")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = -1;
        }
        // Decreases user's Defense and Special Defense by one stage.
        else if (move.name == "Close Combat")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.selfStageAction = true;
            moveInfo.addToDefenseStage = -1;
            moveInfo.addToSpDefenseStage = -1;
        }
        // May lower opponent's Speed by one stage.
        else if (move.name == "Constrict")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = -1;
        }
        // May decrease opponent's Defense by one stage.
        else if (move.name == "Crush Claw")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = -1;
        }
        // Decreases user's Special Attack by 2 stages.
        else if (move.name == "Draco Meteor")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.selfStageAction = true;
            moveInfo.addToSpAttackStage = -2;
        }
        // May decrease opponent's Special Defense by on stage.
        else if (move.name == "Earth Power")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = -1;
        }
        // Decreases opponent's Speed by one stage.
        else if (move.name == "Icy Wind")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = -1;
        }
        // Decreases user's Special Attack by two stages.
        else if (move.name == "Leaf Storm")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.selfStageAction = true;
            moveInfo.addToSpAttackStage = -2;
        }

        // Likely to decrease the opponent's Defense by one stage.
        else if (move.name == "Rock Smash")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = -1;
        }
        // May increases user's Defense by one stage.
        else if (move.name == "Steel Wing")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = 1;
        }
        // May decrease opponent's Special Defense by one stage.
        else if (move.name == "Energy Ball")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = -1;
        }
        // May decrease opponent's Sp. Attack or Sp. Defense by one stage.
        else if (move.name == "Flash Cannon")
        {
            moveInfo.effectAction = true
            moveInfo.stageAction = true
            if (random.nextInt(2) == 1)
                moveInfo.addToSpDefenseStage = -1;
            else
                moveInfo.addToSpAttackStage = -1;
        }
        // May decrease opponent's Defense by one stage.
        else if (move.name == "Iron Tail")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToDefenseStage = -1;
        }
        // Decreases user's Attack and Defense by one stage each.
        else if (move.name == "Superpower")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = -1;
            moveInfo.addToDefenseStage = -1;
            moveInfo.selfStageAction = true;
        }
        // May decrease opponent's Special Defense by one stage.
        else if (move.name == "Focus Blast" || move.name == "Luster Purge")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = -1;
        }
        // Increases user's Attack by one stage when hit.
        else if (move.name == "Rage")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 1;
        }
        // May increase the user's Attack by one stage.
        else if (move.name == "Metal Claw")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 1;
        }
        // May decrease the opponent's Special Defense by one stage.
        else if (move.name == "Seed Flare")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpDefenseStage = -1;
        }
        // May increase user's Attack by one stage.
        else if (move.name == "Meteor Mash")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToAttackStage = 1;
        }
        // Likely to decrease opponent's Special Attack by one stage.
        else if (move.name == "Mist Ball")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpAttackStage = -1;
        }

        // Decreases the opponent's Speed by one stage, if it hits.
        else if (move.name == "Rock Tomb" || move.name == "Mud Shot")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.addToSpeedStage = -1;
        }
        // Decreases user's Special Attack by two stages.
        else if (move.name == "Overheat")
        {
            moveInfo.effectAction = true;
            moveInfo.stageAction = true;
            moveInfo.selfStageAction = true;
            moveInfo.addToSpAttackStage = -2;
        }
        // High Critical-Hit ratio.
        else if (move.name == "Aeroblast" || move.name == "Air Cutter" || move.name == "Attack Order" || move.name == "Crabhammer" || move.name == "Cross Chop" || move.name == "Karate Chop" || move.name == "Night Slash" || move.name == "Stone Edge" || move.name == "Leaf Blade" || move.name == "Psycho Cut" || move.name == "Razor Leaf" || move.name == "Shadow Claw" || move.name == "Slash" || move.name == "Spacial Rend") //
        {
            Critical.addCriticalStage(1,attackingFightPlayer)
            // :TODO effect action?
        }
        // High Critical-Hit ratio. May poison the opponent.
        else if (move.name == "Poison Tail")
        {
            Critical.addCriticalStage(1,attackingFightPlayer);
            moveInfo.effectAction = true;
            moveInfo.poisonAction = true;
        }
        // High Critical-Hit ratio. May cause a burn.
        else if (move.name == "Blaze Kick")
        {
            Critical.addCriticalStage(1,attackingFightPlayer);
            moveInfo.effectAction = true;
            moveInfo.burnAction = true;
        }
        // May cause flinching.
        else if (move.name == "Astonish" || move.name == "Air Slash" || move.name == "Bite" || move.name == "Bone Club" || move.name == "Dark Pulse" || move.name == "Dragon Rush" || move.name == "Extrasensory" || move.name == "Headbutt" || move.name == "Hyper Fang" || move.name == "Iron Head" || move.name == "Needle Arm" || move.name == "Rock Slide" || move.name == "Rolling Kick" || move.name == "Stomp" || move.name == "Waterfall" || move.name == "Zen Headbutt" || move.name == "Twister")
        {
            moveInfo.effectAction = true;
            moveInfo.flinchAction = true;
        }
        // 262  special move May decrease opponent's Accuracy by one stage.
        else if (move.name == "Muddy Water" ||move.name == "Octazooka" || move.name == "Mirror Shot" || move.name == "Mud Bomb")
        {
            moveInfo.addToAccuracyStage = -1;
            moveInfo.effectAction = true;
        }
        // Lowers opponent's accuracy by one stage
        else if (move.name == "Mud-Slap")
        {
            moveInfo.addToAccuracyStage = -1;
            moveInfo.effectAction = true;
        }

        // Power doubles if opponent's HP is less than 50%.
        else if (move.name == "Brine")
        {
            if (defendingFightPlayer.hp < (defendingFightPlayer.maxHp / 2))
            {
                moveInfo.defaultAttackPower = false;
                moveInfo.attackPower = move.power * 2;
            }
        }
        // Likely to confuse the opponent.
        else if (move.name == "Chatter" || move.name == "Confusion" || move.name == "Dizzy Punch" || move.name == "Dynamicpunch" || move.name == "Psybeam" || move.name == "Rock Climb" || move.name == "Signal Beam" || move.name == "Water Pulse") //
        {
            moveInfo.effectAction = true;
            moveInfo.confusionAction = true;
        }
        // 74  physical move Likely to land a Critical Hit, may poison opponent.
        else if (move.name == "Cross Poison")
        {
            moveInfo.effectAction = true;
            moveInfo.poisonAction = true;
            Critical.addCriticalStage(1,attackingFightPlayer);
        }
        // An instant 1-hit KO, if it hits.
        else if (move.name == "Fissure" || move.name == "Guillotine" || move.name == "Horn Drill" || move.name == "Sheer Cold") //
        {
            moveInfo.oneHitKO = true;
        }
        // User receives recoil damage.
        else if (move.name == "Brave Bird" || move.name == "Double-Edge" || move.name == "Flare Blitz" || move.name == "Head Smash" || move.name == "Submission" || move.name == "Take Down" || move.name == "Volt Tackle" || move.name == "Wood Hammer") //
        {
            moveInfo.recoil = 12.5;
        }
        // 106 Dream Eater special move Can only be used on a sleeping target. User recovers half of the damage inflicted on opponent.
        else if (move.name == "Dream Eater")
        {
            if (defendingFightPlayer.sleep == 0)
            {
                fight.roundResult.battleActions.add(new MessageLog("Dream Eater can only be used at an sleeping target."))
                moveInfo.canNotUseAction = true;
            }
        }
        // 447 Wake-up Slap physical move Power doubles if used on a sleeping opponent, but wakes it up.
        else if (move.name == "Wake-up Slap")
        {
            if (defendingFightPlayer.sleep != 0)
            {
                moveInfo.defaultAttackPower = false;
                moveInfo.attackPower = move.power * 2;
                defendingFightPlayer.sleep = 0;
                fight.roundResult.battleActions.add(new MessageLog("${defendingFightPlayer.ownerPokemon.pokemon.name} wakes up."))
            }
        }
        // Trump Card special move The lower the PP, the higher the power.
        else if (move.name == "Trump Card")
        {
            def ownerMoves = OwnerMove.findAllByOwnerAndPartyPositionGreaterThen(attackingFightPlayer.owner,0)      // :TODO fix
            int ppLeft = ownerMoves.sum { it.move.name == "Trump Card"?it.ppLeft:0 }

            moveInfo.defaultAttackPower = false
            if (ppLeft > 4)
                moveInfo.attackPower = 40;
            else if (ppLeft == 4)
                moveInfo.attackPower = 50;
            else if (ppLeft == 3)
                moveInfo.attackPower = 60;
            else if (ppLeft == 2)
                moveInfo.attackPower = 75;
            else if (ppLeft == 1)
                moveInfo.attackPower = 190;

        }
        // Explosion status move User faints.
        else if (move.name == "Explosion" || move.name == "Selfdestruct")
        {
            attackingFightPlayer.hp = 0;
        }
        //400 Super Fang physical move Always takes off half of the opponent's HP.
        else if (move.name == "Super Fang")
        {
            moveInfo.calculateDamage = false;
            moveInfo.damage = Math.round(defendingFightPlayer.hp / 2);
        }
        // 463 Wring Out special move The higher the opponent's HP, the higher the damage.
        else if (move.name == "Wring Out" || move.name == "Crush Grip")
        {
            moveInfo.defaultAttackPower = false;
            moveInfo.attackPower = Math.round(110 * attackingFightPlayer.hp / attackingFightPlayer.maxHp);
        }
        // May cause freezing.
        else if (move.name == "Blizzard" || move.name == "Ice Beam" || move.name == "Ice Punch" || move.name == "Powder Snow") //
        {
            moveInfo.effectAction = true
            moveInfo.freezeAction = true
        }
        // 196 Ice Fang physical move May cause freezing and flinching.
        else if (move.name == "Ice Fang")
        {
            moveInfo.effectAction = true
            moveInfo.freezeAction = true
            moveInfo.flinchAction = true
        }
        // 433 Tri Attack special move May cause paralysis, freezing, or a burn
        else if (move.name == "Tri Attack")
        {
            moveInfo.effectAction = true

            int r = random.nextInt(3)
            if (r == 1)
                moveInfo.freezeAction = true
            else if (r == 2)
                moveInfo.burnAction = true
            else
                moveInfo.paralysisAction = true
        }
        // Cannot miss, regardless of Accuracy and Evasiveness.
        else if (move.name == "Shadow Punch" || move.name == "Faint Attack" || move.name == "Magical Leaf" || move.name == "Magnet Bomb" || move.name == "Aerial Ace") //
        {
            moveInfo.cantMiss = true
        }
        else if (move.name == "Struggle")
        {
            moveInfo.recoil = 50
        }
        else if (move.name == "Rapid Spin")
        {
            attackingFightPlayer.continueMove = 0
            attackingFightPlayer.holdMove = 0
            //attackingFightPlayer.continueTurns = 0; :TODO whats this?
        }
        else if (move.name == "Solarbeam")
        {
            if (attackingFightPlayer.lastBattleAction in MoveAction && attackingFightPlayer.lastBattleAction.move.name != "Solarbeam"){

                fight.roundResult.battleActions.add(new MessageLog("${attackingFightPlayer.ownerPokemon.pokemon.name} gathers light.;"))
                moveInfo.damageAction = false // do nothing
            }
            else
            {
                attackingFightPlayer.battleAction = null // zet op 0 zodat deze niet wordt herhaalt
            }
        }
        // Increases user's Defense one stage in the 1st turn, attacks in the 2nd turn.
        else if (move.name == "Skull Bash")
        {

            if (attackingFightPlayer.lastBattleAction in MoveAction && attackingFightPlayer.lastBattleAction.move.name != "Skull Bash"){
                moveInfo.effectAction = true
                moveInfo.stageAction = true
                moveInfo.addToDefenseStage = 1
                moveInfo.damageAction = false // do nothing
            }
            else
            {
                attackingFightPlayer.battleAction = null; // zet op 0 zodat deze niet wordt herhaalt
            }
        }

        else if (move.name == "Bind")
        {
            moveInfo.holdMove = true;
        }


    }
    
}
