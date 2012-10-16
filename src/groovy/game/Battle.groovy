package game

import game.fight.status.Poison
import game.fight.status.Burn

class Battle {

    /**
     * If both player moves are set we can do a round.
     * @param fight
     */
    public static void battle(Fight fight)
    {
        boolean player1first = BattleOrder.player1First(fight)
        FightPlayer fightPlayer1 = fight.fightPlayer1
        FightPlayer fightPlayer2 = fight.fightPlayer2
        Random random = new Random()



        if (player1first)
        {
            // Speler 1 begint
            attack(fight,fightPlayer1,fightPlayer2,true)

            checkPokemonFainted(fight)

            if (!fight.battleOver){
                attack(fight,fightPlayer2,fightPlayer1,false)

                checkPokemonFainted(fight)
            }
        }
        else
        {
            // Speler 2 begint
            attack(fight,fightPlayer2,fightPlayer1,true)
            checkPokemonFainted(fight)

            if (!fight.battleOver){
                attack(fight,fightPlayer1,fightPlayer2,false)
                checkPokemonFainted(fight)
            }
        }

        if (!fight.battleOver){
            afterBattle(fight,fightPlayer1,fightPlayer2)
            afterBattle(fight,fightPlayer2,fightPlayer1)

            // Check again after the affterBattle effects
            checkPokemonFainted(fight)

            afterTurn(fight)
        }

    }

    static void afterTurn(Fight fight)
    {

        // hp kan niet lager dan 0
        if (fight.fightPlayer1.hp < 0)
            fight.fightPlayer1.hp = 0;
        if (fight.fightPlayer2.hp < 0)
            fight.fightPlayer2.hp = 0;

        // last moves
        fight.fightPlayer1.lastMove = fight.fightPlayer1.move
        fight.fightPlayer2.lastMove = fight.fightPlayer2.move

        // zet moves weer op 0
        fight.fightPlayer1.move = null
        fight.fightPlayer2.move = null

        fight.fightPlayer1.doNoMove = false
        fight.fightPlayer2.doNoMove = false
    }

    static void afterBattle(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer)
    {

        // continues effects
        if (attackFightPlayer.continueMove != 0)
        {
            // :TODO implement
//            continueMove = attackFightPlayer.continueMove;
//            include("continueMove.php");
        }

        // burn
        if (attackFightPlayer.burn == 1)
        {
            // 1/8ste schade
            int burnDamage = Math.floor(attackFightPlayer.maxHp / 8);
            attackFightPlayer.hp = attackFightPlayer.hp - burnDamage;
            fight.log += "m:" + attackFightPlayer.ownerPokemon.pokemon.name + " is hurt by its burn burnDamage.;"
            Recover.healthSlideLogAction(fight, attackFightPlayer,burnDamage);
        }

        // poison wanneer dit niet is gezet
        if (attackFightPlayer.poison == 1)
        {
            // 1/8ste schade
            int poisonDamage = Math.floor(attackFightPlayer.maxHp / 8);
            attackFightPlayer.hp = attackFightPlayer.hp - poisonDamage;
            fight.log += "m:" + attackFightPlayer.ownerPokemon.pokemon.name + " hurts from poison poisonDamage.;";
            Recover.healthSlideLogAction(fight, attackFightPlayer,poisonDamage);
        }

        // badly poisond
        if (attackFightPlayer.badlypoisond > 0)
        {
            // steeds verhoogt met 1/16
            int badlyPoisonDamage = Math.floor(attackFightPlayer.maxHp / 16 * attackFightPlayer.badlypoisond);
            attackFightPlayer.hp = attackFightPlayer.hp - badlyPoisonDamage;
            fight.log += "m:" + attackFightPlayer.ownerPokemon.pokemon.name + " hurts from badly poison badlyPoisonDamage.;";
            Recover.healthSlideLogAction(fight, attackFightPlayer,badlyPoisonDamage);
            // verhoog status
            attackFightPlayer.badlypoisond = attackFightPlayer.badlypoisond + 1;

        }
    }

    static boolean checkFainted(Fight fight, FightPlayer fightPlayer)
    {
        if (fightPlayer.hp <= 0)
        {
            fightPlayer.hp = 0;
            fight.log += "m:" + fightPlayer.ownerPokemon.pokemon.name + " fainted.;";
            // stop attacks
            fight.fightPlayer1.move = null;
            fight.fightPlayer2.move = null;

            return true;
        }
        return false;
    }

    static void win(Fight fight, boolean giveEXP = true)
    {
        // battle is over speler 1 wint
        fight.log += "m:" + fight.fightPlayer1.owner.name + " wins.;";

        if(giveEXP){
            EXP.giveEXP(fight,fight.fightPlayer1,fight.fightPlayer2,true);
        }

        Stats.saveStats(fight.fightPlayer1, true);
        fight.battleOver = true
    }

    static void checkPokemonFainted(Fight fight)
    {

        boolean player1fainted = checkFainted(fight,fight.fightPlayer1);
        boolean player2fainted = checkFainted(fight,fight.fightPlayer2);

        if (fight.battleType == BattleType.PVE && player2fainted && !player1fainted)
        {
            // normal win pve
            fight.fightPlayer1.owner.pveBattlesWon += 1;
            win(fight,true);
            fight.battleOver = true
        }
        else if (fight.battleType == BattleType.PVN && player2fainted && !player1fainted)
        {
            fight.battleOver = checkNpcLoses(fight)
        }
        else if (player1fainted) //  && !$player2fainted
        {

            // update pokemon hp
            fight.fightPlayer1.ownerPokemon.hp = 0
            fight.fightPlayer1.ownerPokemon.save(flush: true)

            // kijk of er nog levende pokemon zijn
            def list = OwnerPokemon.findAllByOwnerAndPartyPositionGreaterThanAndHpGreaterThan(fight.fightPlayer1.owner,0,0)

            int alivePokemonNr = list.size()

            if (alivePokemonNr > 0)
            {
                // speler heeft nog levende pokemon

                // Gevecht voorbij na faint van wild
                if (fight.battleType == BattleType.PVE && player2fainted)
                {
                    // telling gewonnen gevechten
                    fight.fightPlayer1.owner.pveBattlesWon += 1

                    win(fight,false)
                    fight.battleOver = true
                }
                else if (fight.battleType == BattleType.PVN && player2fainted)
                {
                    fight.battleOver = checkNpcLoses(fight)
                }
                else
                {

                }
            }
            else //Speler gaat dood
            {
                if (fight.battleType == BattleType.PVE)
                {
                    fight.fightPlayer1.owner.pveBattlesLost += 1
                }
                if (fight.battleType == BattleType.PVN)
                {
                    fight.fightPlayer1.owner.pvnBattlesLost += 1
                }

                // Stuur speler terug naar laatste recover punt
                Player player = (Player)fight.fightPlayer1.owner
                RecoverAction recoverAction = player.lastRecoverAction

                player.map = recoverAction.map
                player.positionX = recoverAction.positionX
                player.positionY = recoverAction.positionY
                player.save()

                fight.log += "m:You lose, your pokemon have been recovered in town.;";
                Recover.recoverParty(fight.fightPlayer1.owner)
                fight.battleOver = true

            }
        }

    }

    static public boolean checkNpcLoses(Fight fight)
    {

        FightPlayer fightPlayer1 = fight.fightPlayer1

        // geef xp voor verslaan
        EXP.giveEXP(fight,fightPlayer1,fight.fightPlayer2,false);

        // haal volgende op als deze bestaat
        OwnerPokemon nextOwnerPokemon = OwnerPokemon.findByOwnerAndPartyPosition(fightPlayer1.owner,fight.fightPlayer2.ownerPokemon.partyPosition + 1)

        if (!nextOwnerPokemon)
        {
            // Gevechts telling
            fightPlayer1.owner.pvnBattlesWon += 1;

            fight.log += "m:Defeated NPC!.;";
            win(fight, true)

            return true
        }
        else
        {
            // Log de switch
            fight.log += "m:NPC brings out " + nextOwnerPokemon.pokemon.name + ".;";

            fight.fightPlayer2 = Stats.setBaseStats(fight,nextOwnerPokemon, PlayerType.npc, 2);

            return false
        }
    }

    public static void attack(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer, boolean firstMove)
    {

        MoveInfo moveInfo = new MoveInfo()

        Move attackMove = attackFightPlayer.move
        OwnerPokemon attackOwnerPokemon = attackFightPlayer.ownerPokemon
        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        Random random = new Random()

        String attackMovetype
        String attackMovecategory


        // Kijk of er een move wordt gedaan
        if (attackFightPlayer.move)
        {

            moveInfo.criticalHitStage += attackFightPlayer.criticalStage;

            fight.log += "m:" + attackFightPlayer.ownerPokemon.pokemon.name + " uses " + attackMove.name + ".;";

            if (attackMove.category == "physical move" || attackMove.category == "special move")
            {

                moveInfo.effectAction = false

                moveInfo.poisonAction = false
                moveInfo.badlypoisondAction = false
                moveInfo.stageAction = false

                FightMove.getMoveInfo(moveInfo, attackFightPlayer.move, fight, attackFightPlayer, defendingFightPlayer)
            }
            else if (attackMove.category == "status move")
            {
                moveInfo.damageAction = false
                moveInfo.recoverAction = false

                StatusMove.getMoveInfo(moveInfo, attackFightPlayer.move, fight, attackFightPlayer, defendingFightPlayer)
            }

            // openent moves die ervoor zogen dat er niet gemist kan worden
            if (defendingFightPlayer.move?.name == "Solarbeam")
            {
                moveInfo.cantMiss = true
            }

            // bereken accuracy
            int accuracy = Accuraccy.getAccuracy(attackMove.accuracy,attackFightPlayer.accuracyStage);

            // evade over accuracy
            int chanceOnHitting = Evasion.getEvasion(accuracy,defendingFightPlayer.evasionStage);

            boolean moveSucces = false;
            if (random.nextInt(100)+1 <= chanceOnHitting || moveInfo.cantMiss)
            {
                moveSucces = true;
            }

            if (moveInfo.damageAction)
            {
                for (int i=0;i<moveInfo.loop;i++)
                {

                    if (moveSucces && !moveInfo.canNotUseAction)
                    {

                        if (moveInfo.calculateDamage && !moveInfo.oneHitKO)
                        {

                            // attackpower
                            if (moveInfo.defaultAttackPower)
                            {
                                moveInfo.attackPower = attackMove.power
                            }

                            // 277 Payback physical move Power doubles if the user was attacked first.
                            if (attackMove.id == 277 && !firstMove)
                            {
                                moveInfo.attackPower = moveInfo.attackPower * 2
                            }

                            // effectieviteit
                            moveInfo.effectiveness = Effective.effectiveness(attackMove.type,attackOwnerPokemon.pokemon.type1,attackOwnerPokemon.pokemon.type2)

                            if (attackMove.type == "special move")
                            {
                                // bereken nieuwe stats
                                int attackStat = Stat.getStat(attackFightPlayer.spAttack, attackFightPlayer.spAttackStage);
                                int defenseStat = Stat.getStat(defendingFightPlayer.spDefense, defendingFightPlayer.spDefenseStage);
                                // bereken schade
                                moveInfo.damage = Damage.calcDmg(attackFightPlayer.level,attackStat,moveInfo.attackPower,defenseStat,moveInfo.effectiveness);
                            }
                            else
                            {

                                // bereken nieuwe stats
                                int attackStat = Stat.getStat(attackFightPlayer.attack, attackFightPlayer.attackStage);
                                int defenseStat = Stat.getStat(defendingFightPlayer.defense, defendingFightPlayer.defenseStage);

                                // bereken schade
                                moveInfo.damage = Damage.calcDmg(attackFightPlayer.level,attackStat,moveInfo.attackPower,defenseStat,moveInfo.effectiveness);
                            }


                            // User recovers half of the damage inflicted on opponent.
                            if (attackMove.name == "Drain Punch" || attackMove.name == "Absorb" || attackMove.name == "Giga Drain" || attackMove.name == "Leech Life" || attackMove.name == "Mega Drain" || attackMove.name == "Dream Eater")
                            {
                                moveInfo.recoverAction = true;
                                moveInfo.effectAction = true;
                                moveInfo.recover = moveInfo.damage / 2;
                                if (moveInfo.recover < 1)
                                    moveInfo.recover = 1;
                            }

                        }
                        else if (moveInfo.oneHitKO)
                        {
                            defendingFightPlayer.hp = 0;
                            fight.log += "m:It\'s a one hit KO!.;";
                        }

                        if (!moveInfo.oneHitKO)
                        {
                            // Critcal hits
                            if (CriticalHit.tryCriticalHit(moveInfo.criticalHitStage))
                            {
                                moveInfo.damage = moveInfo.damage * 2;
                                fight.log += "m:" + "Critical hit!.;";
                            }

                            moveInfo.damage = Math.floor(moveInfo.damage);

                            // acties na schade berekening

                            // Always leaves opponent with at least 1 HP.
                            if (attackMove.name == "False Swipe")
                            {
                                if (defendingFightPlayer.hp - moveInfo.damage < 0)
                                {
                                    moveInfo.damage = defendingFightPlayer.hp - 1;
                                }
                            }
                            // 2x damage against an opponent using Fly.
                            if (attackMove.name == "Twister" && (defendingOwnerPokemon.pokemon.type1 == "flying" || defendingOwnerPokemon.pokemon.type2 == "flying"))
                            {
                                moveInfo.damage = moveInfo.damage * 2;
                            }

                            if (i == 0)
                            {
                                // bericht effectiveness
                                if (moveInfo.effectiveness == 0)
                                    fight.log += "m:" + "It has no effect.;";
                                else if (moveInfo.effectiveness == 0.25 || moveInfo.effectiveness == 0.5)
                                    fight.log += "m:" + "It`s not very effective.;";
                                else if (moveInfo.effectiveness == 2 || moveInfo.effectiveness == 4)
                                    fight.log += "m:" + "It`s super effective.;";
                            }

                            fight.log += "m:" + attackOwnerPokemon.pokemon.name + " hits " + defendingOwnerPokemon.pokemon.name + " with " + attackMove.name + " ${moveInfo.damage} dmg.;";
                            Recover.healthSlideLogAction(fight, defendingFightPlayer,moveInfo.damage);
                            // Doe schade
                            defendingFightPlayer.hp = defendingFightPlayer.hp - moveInfo.damage;

                            if (moveInfo.recoil)
                            {
                                moveInfo.damage = Math.floor(moveInfo.damage / 100 * moveInfo.recoil);
                                attackFightPlayer.hp = Math.round(attackFightPlayer.hp - moveInfo.damage);
                                fight.log += "m:" + attackOwnerPokemon.pokemon.name + " hurts from recoil damage. ${moveInfo.damage} dmg.;";
                                Recover.healthSlideLogAction(fight, attackFightPlayer,moveInfo.damage);
                                fight.log += "m:Recoil did ${moveInfo.damage} dmg.;";
                            }

                            // Bericht bij loop
                            if (moveInfo.loop > 1 && moveInfo.loop == (i + 1))
                            {
                                fight.log += "m:" + "Hits " + (i + 1) + " times;";
                            }

                        }
                    }
                    else if (moveInfo.canNotUseAction)
                    {

                    }
                    else
                    {
                        // Gemist
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " avoided the attack.;";
                    }

                }

            }

            // Kijk of een aanval een status opheft
            if (attackMovetype == "fire" && defendingFightPlayer.freeze == 1)
            {
                defendingFightPlayer.freeze == 0;
                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is no longer frozen.;";
            }

            // set hold move
            if (moveInfo.holdMove && moveSucces)
            {
                // bij 0 kan de move gezet worden
                if (attackFightPlayer.holdTurns == 0)
                {
                    attackFightPlayer.holdMove = attackFightPlayer.move;
                    attackFightPlayer.holdTurns = random.nextInt(3)+2;

                }
                // bij 1, reset de boel
                else if (attackFightPlayer.holdTurns == 1)
                {
                    attackFightPlayer.holdMove = 0;
                    attackFightPlayer.holdTurns = 0;
                }
            }


            // Kijk of het effect slaagt
            moveInfo.effectSucces = false
            if (moveInfo.attackMoveeffectProb == 0 || random.nextInt(100)+1 <= moveInfo.attackMoveeffectProb)
            {
                if (moveInfo.effectAction && attackMovecategory == "status move")
                {
                    moveSucces = true
                    moveInfo.effectSucces = true
                }
                else if (moveSucces)
                {
                    moveInfo.effectSucces = true
                }
            }

            if ((moveInfo.effectAction && moveSucces))
            {
                // continues move
                if (moveInfo.continueMove && moveInfo.effectSucces)
                {
                    attackFightPlayer.continueMove = attackMove
                }

                // flinch
                if (moveInfo.flinchAction && moveInfo.effectSucces)
                {
                    moveInfo.flinch = true
                }

                // accuracy
                if (moveInfo.addToAccuracyStage != 0 && moveInfo.effectSucces)
                {
                    Accuraccy.addToAccuracyStage(fight,moveInfo.addToAccuracyStage,attackFightPlayer,defendingFightPlayer)
                }

                // sleep
                if (moveInfo.sleepAction && moveInfo.effectSucces)
                {
                    // kijk of de tegenstander al niet slaapt
                    if (defendingFightPlayer.sleep > 0)
                    {
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already asleep.;";
                    }
                    // laat de tegenstander slapen
                    else
                    {
                        Recover.removeAllStatusAfflictions(defendingFightPlayer);
                        defendingFightPlayer.sleep = random.nextInt(6)+2
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " fell asleep.;";
                        moveInfo.sleepActionSucces = true;
                    }
                }

                // confusion
                if (moveInfo.confusionAction && moveInfo.effectSucces)
                {
                    // kijk of de tegenstander confusion is
                    if (defendingFightPlayer.confusion > 0)
                    {
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name  + " is already confused.;";
                    }
                    // confusion
                    else
                    {
                        Recover.removeAllStatusAfflictions(defendingFightPlayer);
                        defendingFightPlayer.confusion = random.nextInt(3)+2;
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name  + " became confused.;";
                        moveInfo.confusionActionSucces = true
                        if (moveInfo.effectActionOnBoth)
                        {
                            Recover.removeAllStatusAfflictions(attackFightPlayer);
                            attackFightPlayer.confusion = random.nextInt(3)+2;
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " became confused.;";
                        }
                    }
                }

                // paralysis
                if (moveInfo.paralysisAction && moveInfo.effectSucces)
                {
                    if (defendingFightPlayer.paralysis == 0)
                    {
                        Recover.removeAllStatusAfflictions(defendingFightPlayer);
                        defendingFightPlayer.paralysis = 1;
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is paralyzed.;";
                        moveInfo.paralysisActionSucces = true;
                    }
                    else
                    {
                        if (moveInfo.attackMoveeffectProb == 0 || moveInfo.attackMoveeffectProb == 100)
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already paralyzed.;";
                    }
                }

                Burn.burnAction(fight, moveInfo, defendingFightPlayer)

                Freeze.freezeAction(fight, moveInfo, defendingFightPlayer)

                Poison.poisonAction(fight, moveInfo, defendingFightPlayer)

                Poison.badlyPoisondAction(fight, moveInfo, defendingFightPlayer)

                if (moveInfo.recoverAction)
                {
                    if (moveSucces && moveInfo.effectSucces)
                    {
                        Recover.recover(fight,moveInfo.recover, attackFightPlayer);
                    }
                    else
                    {
                        if (moveInfo.attackMoveeffectProb == 0 || moveInfo.attackMoveeffectProb == 100)
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " fails to recover.;";
                    }
                }

                if (moveSucces)
                {
                    if (moveInfo.addToDefenseStage != 0)
                        Stage.modifyStage(fight,"defense",moveInfo.addToDefenseStage, moveInfo.selfStageAction, moveInfo.openentStageAction, moveInfo.effectSucces, attackFightPlayer, defendingFightPlayer);
                    if (moveInfo.addToAttackStage != 0)
                        Stage.modifyStage(fight,"attack",moveInfo.addToAttackStage, moveInfo.selfStageAction, moveInfo.openentStageAction, moveInfo.effectSucces, attackFightPlayer, defendingFightPlayer);
                    if (moveInfo.addToSpDefenseStage != 0)
                        Stage.modifyStage(fight,"spDefense",moveInfo.addToSpDefenseStage, moveInfo.selfStageAction, moveInfo.openentStageAction, moveInfo.effectSucces, attackFightPlayer, defendingFightPlayer);
                    if (moveInfo.addToSpAttackStage != 0)
                        Stage.modifyStage(fight,"spAttack",moveInfo.addToSpAttackStage, moveInfo.selfStageAction, moveInfo.openentStageAction, moveInfo.effectSucces, attackFightPlayer, defendingFightPlayer);
                    if (moveInfo.addToSpeedStage != 0)
                        Stage.modifyStage(fight,"speed",moveInfo.addToSpeedStage, moveInfo.selfStageAction, moveInfo.openentStageAction, moveInfo.effectSucces, attackFightPlayer, defendingFightPlayer);
                }


            }

            // bericht bij falen van effect
            else if (moveInfo.effectAction && !moveSucces)
            {
                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " fails to perform move.;";
            }

            // Flinch
            if (moveInfo.flinch && firstMove)
            {
                defendingFightPlayer.move = null
                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " flinched!.;";
            }
        }

        // after first attack
        if (firstMove)
        {
            // holding
            if (attackFightPlayer.holdMove != 0)
            {
                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is being traped by " + attackMove.name + ".;";
                defendingFightPlayer.move = null;
            }

            // kijk of de volgende actie faalt
            if (moveInfo.paralysisActionSucces)
            {
                if (random.nextInt(4) == 1)
                {
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is paralyzed. It cant move.;";
                    defendingFightPlayer.move = null;
                }
            }
            if (moveInfo.confusionActionSucces)
            {
                if (random.nextInt(2) == 1)
                {
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is confused.;";
                    defendingFightPlayer.move = null;
                }
            }
            if (moveInfo.sleepActionSucces)
            {
                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is a sleep.;";
                defendingFightPlayer.move = null;
            }
            if (moveInfo.freezeActionSucces)
            {
                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is frozen solid!;";
                defendingFightPlayer.move= null;
            }
        }
    }

    public static void beforeChosingMove(Fight fight, FightPlayer fightPlayer, Owner owner)
    {

        // freeze
        Freeze.checkFreeze(fight,fightPlayer);
        // paralyses
        Paralyses.checkParalyses(fight,fightPlayer);
        // confusion
        Confusion.checkConfusion(fight,fightPlayer);
        // sleep
        Sleep.checkSleep(fight,fightPlayer);

        // continue last move
        if (fightPlayer.lastMove?.id == 376 || fightPlayer.lastMove?.id == 360)
        {
//            if (fightPlayer.playerType != PlayerType.wildPokemon){
//
//                fightPlayer.lastMove
//            }
            // :TODO implement
//            $sql = "select id from ownerpokemonmove where moveId = '" . fight.{"player" . $player . "lastMove"} . "' and ownerPokemonId = '" . fight.{"player" . $player . "OwnerPokemonId"} . "'";
//            $row = mysql_fetch_row(DatabaseQuery::Execute($sql));
//
//            $_SESSION["takePP"] = false;
//            header("Location: index.php?doMove=" . $row[0]);
        }


        // struggle
        if (fightPlayer.fight.battleType == BattleType.PVE && fightPlayer == fightPlayer.fight.opponentPlayer(owner))
        {
            // :TODO ??? nothing here?
        }
        else
        {

            if (fightPlayer.holdMove != 0 && fightPlayer.holdTurns != 0)
            {
                // :TODO implement
//                fightPlayer.holdTurns -= 1;
//
//                header("Location: index.php?doMove=" . fight.{"player" . $player . "holdMove"});
//                exit();
            }
            else
            {
//                // Struggle physical move Only usable when all PP are gone. Hurts the user.
//                // Struggle deals damage to the opponent and the user receives recoil damage.
//                // The user takes recoil damage equal to 50% of the damage the attack did to the opponent.
//                $sql = "
//                select id
//                from ownerpokemonmove
//                where ownerPokemonId = '" . fight.{"player" . $player . "OwnerPokemonId"} . "' AND ppLeft > 0";
//
//                $result = DatabaseQuery::execute($sql);
//
//                if (mysql_num_rows($result) == 0)
//                {
//                    header("Location: index.php?doMove=394");
//                    // set move?
//                }
            }
        }

    }



}
