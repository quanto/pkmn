package game

class BattleController {

    def index() {
        boolean debug = true

        Owner owner = session.owner
        //owner = owner.merge()
        Fight fight = owner.fight

        if (owner && fight){
            /*
            // active player en openent player
            if (fight.player1Id == $owner->id)
            {
                $mp = 1;
                $op = 2;
            }
            else
            {
                $mp = 2;
                $op = 1;
            }
            */
            int player1protect = 0
            int player2protect = 0

            render view: 'index', model: [fight:fight]
        }
    }

    def logRequest = {
        Owner owner = session.owner
        //owner = owner.merge()
        Fight fight = owner.fight

        render text: g.render(template: 'log',model: [fight:fight])
    }

    def doMove = {

        Owner owner = session.owner
        //owner = owner.merge()
        Fight fight = owner.fight
        //fight = fight.refresh()


        int ownerPokemonMoveId = Integer.parseInt(params.id)
        // :TODO make safe

        OwnerMove ownerMove = OwnerMove.get(ownerPokemonMoveId)
        int moveId = ownerMove.move.id

        if (moveId == -1 || moveId == 394) // strangle || geen move
        {
            setMove(fight,fight.fightPlayer1, moveId)
        }
        else
        {

//            // Controlleer of de user klopt
//            if (ownerMove.ownerPokemon.owner != owner)
//            {
//                println "4"
//                render text:"Move bestaat niet voor pokemon"
//                return
//            }
//            else
//            {
                // :TODO implement
//                // controlleer pp
//                if (ownerPokemonMove->ppLeft != 0)
//                {
//                    if (isset(_SESSION["takePP"]) && _SESSION["takePP"] == false)
//                    {
//
//                        _SESSION["takePP"] = true;
//                    }
//                    else
//                    {
//                        ownerPokemonMove->ppLeft -= 1;
//                    }
//                    ownerPokemonMove->update();
                    setMove(fight,fight.fightPlayer1,moveId);
//                }
//                else
//                {
//                    exit();
//                    //header("Location: index.php?action=fight");
//                }
                redirect action: "index"
//            }
        }
    }

    def menuRequest = {
        Owner owner = session.owner
        //owner = owner.merge()
        Fight fight = owner.fight
        FightPlayer myFightPlayer = fight.myPlayer(session.owner)

        // wil move leren
//			if (fight.{"player" . $mp . "learnMoves"} != "" && isset($_GET["forgetMove"]) && $_GET["forgetMove"] == "yes")
//			{
//				forgetMoveList();
//			}
//			// leer moves
//			else if (fight.{"player" . $mp . "learnMoves"} != "")
//			{
//				chooseLearnMove();
//			}
//			else if (fight.battleOver == 1)
//			{
//				//displayBattle();
//				/*
//				$quests = new quests();
//				$quests->qd1 = fight.player2PokemonId;
//				$quests->playerid = fight.player1Id;
//
//				$quests->checkQuest();
//				*/
//                 exitMenu();
//            }
//            else if (fight.{"player" . $mp . "Hp"} == 0 && fight.{"player" . $op . "Hp"} != 0)
//            {
//                pokemonList(true);
//            }
//            else if (isset($_GET["item"]) && fight.battleOver == 0)
//            {
//                //displayBattle();
//                itemList();
//            }
//            else if (isset($_GET["fight"]) && fight.battleOver == 0)
//            {
//                beforeChosingMove($mp);
//                moveList();
//            }
//            // Switch pokemon list
//            else if (isset($_GET["action"]) && $_GET["action"] == "pkmn" && fight.battleOver == 0)
//            {
//                pokemonList(false);
//            }
//            else
//            {
//                actionList();
//            }
//            exit();

        // moveList
        if (params.fight != null && !fight.battleOver)
        {
            beforeChosingMove(fight, myFightPlayer, owner);

            List<OwnerMove> ownerMoveList = OwnerMove.findAllByOwnerPokemon(myFightPlayer.ownerPokemon)

            render text: g.render(template: 'moveList', model: [ownerMoveList:ownerMoveList])
        }
        // Switch pokemon list
        else if (params.pkmn != null && !fight.battleOver)
        {
            List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwnerAndPartyPositionGreaterThan(owner,0)
            FightPlayer fightPlayer = fight.fightPlayer1
            render text: g.render(template: 'pokemonList',model: [mustChoose:false,ownerPokemonList:ownerPokemonList,fightPlayer:fightPlayer])
        }
        else {
            render text: g.render(template: 'actionList')
        }
    }

    void checkFreeze(Fight fight, FightPlayer fightPlayer)
    {

        if (fightPlayer.freeze == 1)
        {
            Random r = new Random();
            // Kijk of freeze ophoud
            if (r.nextInt(10)+1 == 1)
            {

                fightPlayer.fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} is no longer frozen.;";
                fightPlayer.freeze = 0;
            }
            else
            {
                fightPlayer.fight.log += "m:${fightPlayer.ownerPokemon.pokemon.name} is frozen solid!;";
                // geen move
//                if ($player != 2 || fightPlayer.fight.battleType != BattleType.PVE)
                    setMove(fight,fightPlayer,-1);
//                else
//                    fight.{"player" . $player . "move"} = -1;
            }
        }
    }

    /**
     * Player sets a move
     */
    void setMove(Fight fight, FightPlayer fightPlayer, int moveId, boolean clearLog = true)
    {

        // reset log
        if (clearLog){
            fight.log = "";
        }

        // :TODO not safe
        fightPlayer.move = Move.get(moveId)

        // Kijk of tegen de computer wordt gespeelt, dan wordt er een move gekozen
        if (fight.battleType == BattleType.PVE || fight.battleType == BattleType.PVN)
        {
            if (fight.battleType == BattleType.PVE)
            {
                // reset escape attempts
                if (moveId != -1){
                    fightPlayer.escapeAttempts = 0;
                }
                // kies random wild move

                fightPlayer.opponentFightPlayer().move = choseWildMove(fightPlayer.opponentFightPlayer());
            }
            else
            {
                // :TODO implement
//                // kies random move van npc
//                fightPlayer.opponentFightPlayer().move = choseNpcMove(); // Testmove gezet
            }
            // overide move door status

            // freeze
            checkFreeze(fight, fightPlayer);
            // :TODO implement
//            // paralyses
//            checkParalyses(2);
//            // confusion
//            checkConfusion(2);
//            // sleep
//            checkSleep(2);
        }

        // Kijk of ronde gedaan kan worden
        if (fight.fightPlayer1.move != null && fight.fightPlayer2.move != null)
        {
            battle(fight)
        }
    }

    boolean isSpeedMove(FightPlayer fightPlayer)
    {
        Move move = fightPlayer.move

        if (!move){
            return false
        }
        //12 Aqua Jet 50 Bullet Punch 121 Extremespeed 198 Ice Shard 226 Mach Punch 306 Quick Attack 351 Shadow Sneak 442 Vacuum Wave
        if (move.id == 12 || move.id == 50 || move.id == 121 || move.id == 198 || move.id == 226 || move.id == 306 || move.id == 351 || move.id == 442)
        {
            return true;
        }
        else {
            return false
        }
    }

    void removeAllStatusAfflictions(FightPlayer fightPlayer)
    {
        fightPlayer.burn = 0;
        fightPlayer.freeze = 0;
        fightPlayer.paralysis = 0;
        fightPlayer.poison = 0;
        fightPlayer.badlypoisond = 0;
        fightPlayer.sleep = 0;
        fightPlayer.confusion = 0;
        fightPlayer.curse = 0;
        // :TODO uknown
       // fightPlayer.attract = 0;
    }

    void battle(Fight fight)
    {
        boolean player1first = false
        FightPlayer fightPlayer1 = fight.fightPlayer1
        FightPlayer fightPlayer2 = fight.fightPlayer2
        Random random = new Random()

        // Bepaal wie begint
        if (isSpeedMove(fight.fightPlayer1))
        {
            if (!isSpeedMove(fight.fightPlayer2))
            {
                player1first = true;
            }
            else
            {
                if (fightPlayer1.speed > fightPlayer2.speed)
                    player1first = true;
                else if (fightPlayer1.speed == fightPlayer1.speed)
                {
                    if (random.nextInt(2) == 1)
                        player1first = true;
                    else
                        player1first = false;
                }
                else
                    player1first = false;
            }
        }
        else if (isSpeedMove(fight.fightPlayer2))
        {
            player1first = false;
        }
        else
        {
            if (fightPlayer1.speed > fightPlayer2.speed)
                player1first = true;
            else if (fightPlayer1.speed == fightPlayer2.speed)
            {
                if (random.nextInt(2) == 1)
                    player1first = true;
                else
                    player1first = false;
            }
            else
                player1first = false;
        }

        boolean battleOver = false

        if (player1first)
        {
            // Speler 1 begint
            attack(fight,fightPlayer1,fightPlayer2,true)

            battleOver = checkPokemonFainted(fight)

            if (!battleOver){
                attack(fight,fightPlayer2,fightPlayer1,false)

                checkPokemonFainted(fight)
            }
        }
        else
        {
            // Speler 2 begint
            attack(fight,fightPlayer2,fightPlayer1,true)
            battleOver = checkPokemonFainted(fight)

            if (!battleOver){
                attack(fight,fightPlayer1,fightPlayer2,false)
                checkPokemonFainted(fight)
            }
        }

        if (!battleOver){
            afterBattle(fight,fightPlayer1,fightPlayer2);
            afterBattle(fight,fightPlayer2,fightPlayer1);

            // Check again after the affterBattle effects
            checkPokemonFainted(fight);

            afterTurn(fight);
        }

    }

    void afterTurn(Fight fight)
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

    }

    void healthSlideLogAction(Fight fight, FightPlayer fightPlayer, int damage)
    {

        if (fightPlayer.hp - damage < 0)
        {
            damage = fightPlayer.hp
        }
        /*
          else if (fightPlayer.Hp"} - $damage > fightPlayer.MaxHp"})
              $damage = fightPlayer.Hp"} - fightPlayer.MaxHp"};
          */
        fightPlayer.fight.log += "a:${fightPlayer.playerNr}:${damage};";
    }

    void afterBattle(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer)
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
            healthSlideLogAction(fight, attackFightPlayer,burnDamage);
        }

        // poison wanneer dit niet is gezet
        if (attackFightPlayer.poison == 1)
        {
            // 1/8ste schade
            int poisonDamage = Math.floor(attackFightPlayer.maxHp / 8);
            attackFightPlayer.hp = attackFightPlayer.hp - poisonDamage;
            fight.log += "m:" + attackFightPlayer.ownerPokemon.pokemon.name + " hurts from poison poisonDamage.;";
            healthSlideLogAction(fight, attackFightPlayer,poisonDamage);
        }

        // badly poisond
        if (attackFightPlayer.badlypoisond > 0)
        {
            // steeds verhoogt met 1/16
            int badlyPoisonDamage = Math.floor(attackFightPlayer.maxHp / 16 * attackFightPlayer.badlypoisond);
            attackFightPlayer.hp = attackFightPlayer.hp - badlyPoisonDamage;
            fight.log += "m:" + attackFightPlayer.ownerPokemon.pokemon.name + " hurts from badly poison badlyPoisonDamage.;";
            healthSlideLogAction(fight, attackFightPlayer,badlyPoisonDamage);
            // verhoog status
            attackFightPlayer.badlypoisond = attackFightPlayer.badlypoisond + 1;

        }
    }

    boolean checkFainted(Fight fight, FightPlayer fightPlayer)
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

    void win(Fight fight, boolean giveEXP = true)
    {
        // battle is over speler 1 wint
        fight.log += "m:" + fight.fightPlayer1.owner.name + " wins.;";

        if(giveEXP){
            EXP.giveEXP(fight,fight.fightPlayer1,fight.fightPlayer2,true);
        }

        saveStats(fight.fightPlayer1, true);
        fight.battleOver = 1
    }

    /**
     * Uitgevoerd bij het switchen van pokemon
     * En na het gevecht, na het gevecht stage weer op 0
     */
    void saveStats(FightPlayer fightPlayer,boolean endBattle = false)
    {
        OwnerPokemon ownerPokemon = fightPlayer.ownerPokemon

        ownerPokemon.hp = fightPlayer.hp
        ownerPokemon.level = fightPlayer.level
        ownerPokemon.burn = fightPlayer.burn
        ownerPokemon.freeze = fightPlayer.freeze
        ownerPokemon.paralysis = fightPlayer.paralysis
        ownerPokemon.badlyPoisond = fightPlayer.badlypoisond
        ownerPokemon.poison = fightPlayer.poison
        ownerPokemon.sleep = fightPlayer.sleep
        ownerPokemon.confusion = fightPlayer.confusion
        ownerPokemon.curse = fightPlayer.curse

        if (!endBattle)
        {
            ownerPokemon.attackStage = fightPlayer.attackStage
            ownerPokemon.defenseStage = fightPlayer.defenseStage
            ownerPokemon.spAttackStage = fightPlayer.spAttackStage
            ownerPokemon.spDefenseStage = fightPlayer.spDefenseStage
            ownerPokemon.speedStage = fightPlayer.speedStage
            ownerPokemon.accuracyStage = fightPlayer.accuracyStage
            ownerPokemon.criticalStage = fightPlayer.criticalStage
            ownerPokemon.evasionStage = fightPlayer.evasionStage
        }
        else
        {
            ownerPokemon.attackStage = 0;
            ownerPokemon.defenseStage = 0;
            ownerPokemon.spAttackStage = 0;
            ownerPokemon.spDefenseStage = 0;
            ownerPokemon.speedStage = 0;
            ownerPokemon.accuracyStage = 0;
            ownerPokemon.criticalStage = 0;
            ownerPokemon.evasionStage = 0;
        }
        ownerPokemon.save()
    }

    boolean checkPokemonFainted(Fight fight)
    {
        boolean battleOver = false

        boolean player1fainted = checkFainted(fight,fight.fightPlayer1);
        boolean player2fainted = checkFainted(fight,fight.fightPlayer2);

        if (fight.battleType == BattleType.PVE && player2fainted && !player1fainted)
        {
            // normal win pve
            fight.fightPlayer1.owner.pveBattlesWon += 1;
            win(fight,true);
            battleOver = true
        }
        else if (fight.battleType == BattleType.PVN && player2fainted && !player1fainted)
        {
            // :TODO implement
            //checkNpcLoses();
        }
        else if (player1fainted) //  && !$player2fainted
        {

            // update pokemon hp
            fight.fightPlayer1.ownerPokemon.hp = 0
            // kijk of er nog levende pokemon zijn
            int alivePokemonNr = OwnerPokemon.countByOwnerAndPartyPositionGreaterThanAndHpGreaterThan(fight.fightPlayer1.owner,0,0)

            if (alivePokemonNr > 0)
            {
                // speler heeft nog levende pokemon

                // Gevecht voorbij na faint van wild
                if (fight.battleType == BattleType.PVE && player2fainted)
                {
                    // telling gewonnen gevechten
                    fight.fightPlayer1.owner.pveBattlesWon += 1;

                    win(fight,false);
                    battleOver = true
                }
                else if (fight.battleType == BattleType.PVN && player2fainted)
                {
                    // :TODO implement
                    //checkNpcLoses();
                }
                else
                {
                    // :TODO do something
                    //header("Location: index.php");
                    //exit(0);
                }
            }
            else //Speler gaat dood
            {
                if (fight.battleType == BattleType.PVE)
                {
                    fight.fightPlayer1.owner.pveBattlesLost += 1;
                }
                if (fight.battleType == BattleType.PVN)
                {
                    fight.fightPlayer1.owner.pvnBattlesLost += 1;
                }

                // :TODO implement
//                /**
//                 * todo: kies dichtsbijzijnde map
//                 */
//                $pokeCenters = new pokeCenters();
//                $pokeCenters->id = 1;
//                $pokeCenters->get();
//
//                $owner->map = $pokeCenters->mapName;
//                $owner->positionX = $pokeCenters->positionX;
//                $owner->positionY = $pokeCenters->positionY;
//                $owner->update();
//
//                $fight->log .= "m:You lose, your pokemon have been recovered in town.;";
//                recoverParty($fight->player1Id);
//                $fight->battleOver = 1;
//                $fight->update();
//                //header("Location: index.php");
//                exit(0);
                battleOver = true
            }
            return battleOver
        }

    }

    void attack(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer, boolean firstMove)
    {

        // Start waarden
        int criticalHitStage = 1;
        int addToAccuracyStage = 0;
        boolean canNotUseAction = false;
        boolean oneHitKO = false;
        boolean cantMiss = false;
        boolean recoil = false;
        boolean flinch = false;
        boolean paralysisActionSucces = false;
        boolean confusionActionSucces = false;
        boolean sleepActionSucces = false;
        boolean freezeActionSucces = false;
        boolean flinchAction = false;
        boolean sleepAction = false;
        boolean confusionAction = false;
        boolean effectSucces = false;
        boolean burnAction = false;
        boolean freezeAction = false;
        boolean paralysisAction = false;
        boolean poisonAction = false;
        boolean badlypoisondAction = false;
        boolean addToDefenseStage = false;
        boolean selfStageAction = false;
        boolean openentStageAction = false;
        boolean addToAttackStage = false;
        boolean addToSpDefenseStage = false;
        boolean addToSpAttackStage = false;
        boolean addToSpeedStage = false;
        double effectiveness = 1;
        boolean effectAction = false;
        boolean stageAction = false;
        boolean holdMove = false;
        boolean continueMove = false;
        Move attackMove = attackFightPlayer.move
        OwnerPokemon attackOwnerPokemon = attackFightPlayer.ownerPokemon
        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        Random random = new Random()

        boolean damageAction = true;
        boolean recoverAction = false;
        int loop = 1;
        boolean calculateDamage = true;
        boolean defaultAttackPower = true; // useDefault?

        int attackPower
        int damage

        // Kijk of er een move wordt gedaan
        if (attackFightPlayer.move)
        {

            criticalHitStage += attackFightPlayer.criticalStage;

            fight.log += "m:" + attackFightPlayer.ownerPokemon.pokemon.name + " uses " + attackMove.name + ".;";

            if (attackMove.category == "physical move" || attackMove.category == "special move")
            {

                effectAction = false;

                poisonAction = false;
                badlypoisondAction = false;
                stageAction = false;

                // :TODO implement
                //include("fightMove.php");
            }
            else if (attackMove.category == "status move")
            {
                damageAction = false;
                recoverAction = false;

                // :TODO implement
                //include("statusMove.php");
            }

            // openent moves die ervoor zogen dat er niet gemist kan worden
            // :TODO implement
//            if (fight.{"player" . defendingFightPlayer . "move"} == 376)
//            {
//                cantMiss = true;
//            }

            // bereken accuracy
            int accuracy = Accuraccy.getAccuracy(attackMove.accuracy,attackFightPlayer.accuracyStage);

            // evade over accuracy
            int chanceOnHitting = Evasion.getEvasion(accuracy,defendingFightPlayer.evasionStage);

            boolean moveSucces = false;
            if (random.nextInt(100)+1 <= chanceOnHitting || cantMiss)
            {
                moveSucces = true;
            }

            if (damageAction)
            {
                for (int i=0;i<loop;i++)
                {

                    if (moveSucces && !canNotUseAction)
                    {

                        if (calculateDamage && !oneHitKO)
                        {

                            // attackpower
                            if (defaultAttackPower)
                            {
                                attackPower = attackMove.power;
                            }

                            // 277 Payback physical move Power doubles if the user was attacked first.
                            if (attackMove.id == 277 && !firstMove)
                            {
                                attackPower = attackPower * 2;
                            }

                            // effectieviteit
                            effectiveness = BattleFunctions.effectiveness(attackMove.type,attackOwnerPokemon.pokemon.type1,attackOwnerPokemon.pokemon.type2)

                            if (attackMove.type == "special move")
                            {
                                // bereken nieuwe stats
                                int attackStat = Stat.getStat(attackFightPlayer.spAttack, attackFightPlayer.spAttackStage);
                                int defenseStat = Stat.getStat(defendingFightPlayer.spDefense, defendingFightPlayer.spDefenseStage);
                                // bereken schade
                                damage = Damage.calcDmg(attackFightPlayer.level,attackStat,attackPower,defenseStat,effectiveness);
                            }
                            else
                            {

                                // bereken nieuwe stats
                                int attackStat = Stat.getStat(attackFightPlayer.attack, attackFightPlayer.attackStage);
                                int defenseStat = Stat.getStat(defendingFightPlayer.defense, defendingFightPlayer.defenseStage);

                                // bereken schade
                                damage = Damage.calcDmg(attackFightPlayer.level,attackStat,attackPower,defenseStat,effectiveness);
                            }


                            // User recovers half of the damage inflicted on opponent.
                            if (attackMove.name == "Drain Punch" || attackMove.name == "Absorb" || attackMove.name == "Giga Drain" || attackMove.name == "Leech Life" || attackMove.name == "Mega Drain" || attackMove.name == "Dream Eater")
                            {
//                                // :TODO implement
//                                recoverAction = true;
//                                effectAction = true;
//                                recover = damage / 2;
//                                if (recover < 1)
//                                    recover = 1;
                            }

                        }
                        else if (oneHitKO)
                        {
                            defendingFightPlayer.hp = 0;
                            fight.log += "m:" . "It\'s a one hit KO!.;";
                        }

                        if (!oneHitKO)
                        {
                            // Critcal hits
                            if (CriticalHit.tryCriticalHit(criticalHitStage))
                            {
                                damage = damage * 2;
                                fight.log += "m:" + "Critical hit!.;";
                            }

                            damage = Math.floor(damage);

                            // acties na schade berekening

                            // Always leaves opponent with at least 1 HP.
                            if (attackMove.name == "False Swipe")
                            {
                                if (defendingFightPlayer.hp - damage < 0)
                                {
                                    damage = defendingFightPlayer.hp - 1;
                                }
                            }
                            // 2x damage against an opponent using Fly.
                            if (attackMove.name == "Twister" && (defendingOwnerPokemon.pokemon.type1 == "flying" || defendingOwnerPokemon.pokemon.type2 == "flying"))
                            {
                                damage = damage * 2;
                            }

                            if (i == 0)
                            {
                                // bericht effectiveness
                                if (effectiveness == 0)
                                    fight.log += "m:" + "It has no effect.;";
                                else if (effectiveness == 0.25 || effectiveness == 0.5)
                                fight.log += "m:" + "It`s not very effective.;";
                                else if (effectiveness == 2 || effectiveness == 4)
                                fight.log += "m:" + "It`s super effective.;";
                            }

                            fight.log += "m:" + attackOwnerPokemon.pokemon.name + " hits " + defendingOwnerPokemon.pokemon.name + " with " + attackMove.name + " ${damage} dmg.;";
                            healthSlideLogAction(fight, defendingFightPlayer,damage);
                            // Doe schade
                            defendingFightPlayer.hp = defendingFightPlayer.hp - damage;

                            if (recoil)
                            {

                                damage = Math.floor(damage / 100 * recoil);
                                attackFightPlayer.hp = Math.round(attackFightPlayer.hp - damage);
                                fight.log += "m:" + attackOwnerPokemon.pokemon.name + " hurts from recoil damage. damage dmg.;";
                                healthSlideLogAction(fight, attackFightPlayer,damage);
                                fight.log += "m:Recoil did damage dmg.;";
                            }

                            // Bericht bij loop
                            if (loop > 1 && loop == (i + 1))
                            {
                                fight.log += "m:" + "Hits " + (i + 1) + " times;";
                            }

                        }
                    }
                    else if (canNotUseAction)
                    {

                    }
                    else
                    {
                        // Gemist
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " avoided the attack.;";
                    }

                }

            }

            // TODO implement
//            // Kijk of een aanval een status opheft
//            if (attackMovetype == "fire" && defendingFightPlayer.freeze == 1)
//            {
//                defendingFightPlayer.freeze == 0;
//                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is no longer frozen.;";
//            }

            // set hold move
            if (holdMove && moveSucces)
            {
                // bij 0 kan de move gezet worden
                if (attackFightPlayer.holdTurns == 0)
                {
                    attackFightPlayer.holdMove = $_GET["doMove"];
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
            effectSucces = false;
            // :TODO implement
//            if (attackMoveeffectProb == 0 || random.nextInt(100)+1 <= attackMoveeffectProb)
//            {
//                if (effectAction && attackMovecategory == "status move")
//                {
//                    moveSucces = true;
//                    effectSucces = true;
//                }
//                else if (moveSucces)
//                {
//                    effectSucces = true;
//                }
//            }

            if ((effectAction && moveSucces))
            {
                // continues move
                if (continueMove && effectSucces)
                {
                    attackFightPlayer.continueMove = attackMoveid;
                }

                // flinch
                if (flinchAction && effectSucces)
                {
                    flinch = true;
                }

                // accuracy
                if (addToAccuracyStage != 0 && effectSucces)
                {
                    addToAccuracyStage(addToAccuracyStage,attackFightPlayer,defendingFightPlayer);
                }

                // sleep
                if (sleepAction && effectSucces)
                {
                    // kijk of de tegenstander al niet slaapt
                    if (defendingFightPlayer.sleep > 0)
                    {
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already asleep.;";
                    }
                    // laat de tegenstander slapen
                    else
                    {
                        removeAllStatusAfflictions(defendingFightPlayer);
                        defendingFightPlayer.sleep = random.nextInt(6)+2
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " fell asleep.;";
                        sleepActionSucces = true;
                    }
                }

                // confusion
                if (confusionAction && effectSucces)
                {
                    // kijk of de tegenstander confusion is
                    if (defendingFightPlayer.confusion > 0)
                    {
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name  + " is already confused.;";
                    }
                    // confusion
                    else
                    {
                        removeAllStatusAfflictions(defendingFightPlayer);
                        defendingFightPlayer.confusion = random.nextInt(3)+2;
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name  + " became confused.;";
                        confusionActionSucces = true;
                        if (effectActionOnBoth)
                        {
                            removeAllStatusAfflictions(attackFightPlayer);
                            attackFightPlayer.confusion = random.nextInt(3)+2;
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " became confused.;";
                        }
                    }
                }

                // paralysis
                if (paralysisAction && effectSucces)
                {
                    if (defendingFightPlayer.paralysis == 0)
                    {
                        removeAllStatusAfflictions(defendingFightPlayer);
                        defendingFightPlayer.paralysis = 1;
                        fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is paralyzed.;";
                        paralysisActionSucces = true;
                    }
                    else
                    {
                        if (attackMoveeffectProb == 0 || attackMoveeffectProb == 100)
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already paralyzed.;";
                    }
                }

                // burn
                if (burnAction && effectSucces)
                {
                    if (defendingFightPlayer.burn == 0)
                    {
                        if (defendingOwnerPokemon.pokemon.type1 == "fire" || defendingOwnerPokemon.pokemon.type2 == "fire")
                        {
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name  + " is immune to fire.;";
                        }
                        else
                        {
                            removeAllStatusAfflictions(defendingFightPlayer);
                            defendingFightPlayer.burn = 1;
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name  + " is burning.;";
                            burnActionSucces = true;
                        }
                    }
                    else
                    {
                        if (attackMoveeffectProb == 0 || attackMoveeffectProb == 100)
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already burning.;";
                    }
                }

                // freeze
                if (freezeAction && effectSucces)
                {
                    if (defendingFightPlayer.freeze == 0)
                    {
                        if (defendingOwnerPokemon.pokemon.type1 == "ice" || defendingOwnerPokemon.pokemon.type2 == "ice")
                        {
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is immune to freeze.;";
                        }
                        else
                        {
                            removeAllStatusAfflictions(defendingFightPlayer);
                            defendingFightPlayer.freeze = 1;
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is frozen.;";
                            freezeActionSucces = true;
                        }
                    }
                    else
                    {
                        if (attackMoveeffectProb == 0 || attackMoveeffectProb == 100)
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already frozen.;";
                    }
                }

                // Poison
                if (poisonAction)
                {
                    // controlleer of de status al staat
                    if (defendingFightPlayer.poison == 0 && defendingFightPlayer.badlypoisond == 0 && effectSucces)
                    {
                        if (defendingOwnerPokemon.pokemon.type1 == "poison" || defendingOwnerPokemon.pokemon.type2== "poison" || defendingOwnerPokemon.pokemon.type1 == "steel" || defendingOwnerPokemon.pokemon.type2 == "steel")
                        {
                            fight.log += "m:" . defendingOwnerPokemon.pokemon.name + " is immune to poison.;";
                        }
                        else
                        {
                            removeAllStatusAfflictions(defendingFightPlayer);
                            fight.{"player" . defendingFightPlayer . "poison"} = 1;
                            fight.log += "m:" . defendingOwnerPokemon.pokemon.name + " is poisoned.;";
                            poisonActionSucces = true;
                        }
                    }
                    else
                    {
                        // Bericht bij move die bedoelt is om te poisenen
                        if (attackMoveeffectProb == 0 || attackMoveeffectProb == 100)
                            fight.log += "m:" . defendingOwnerPokemon.pokemon.name + " is already poisoned.;";
                    }
                }

                //badlypoisend wordt meer met iedere beurt
                if (badlypoisondAction)
                {
                    // kijk of niet al poisond
                    if (defendingFightPlayer.poison == 0 && defendingFightPlayer.badlypoisond == 0 && effectSucces)
                    {
                        if (defendingOwnerPokemon.pokemon.type1 == "poison" || defendingOwnerPokemon.pokemon.type2 == "poison" || defendingOwnerPokemon.pokemon.type1 == "steel" || defendingOwnerPokemon.pokemon.type2 == "steel")
                        {
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is immune to poison.;";
                        }
                        else
                        {
                            removeAllStatusAfflictions(defendingFightPlayer);
                            // Zet de status
                            defendingFightPlayer.badlypoisond = 1;
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is badly poisoned.;";
                            badlypoisondActionSucces = true;
                        }
                    }
                    else
                    {
                        // Bericht bij move die bedoelt is om te poisenen
                        if (attackMoveeffectProb == 0 || attackMoveeffectProb == 100)
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is already poisoned.;";
                    }
                }

                if (recoverAction)
                {
                    if (moveSucces && effectSucces)
                    {
                        recover(fight,recover, attackFightPlayer);
                    }
                    else
                    {
                        if (attackMoveeffectProb == 0 || attackMoveeffectProb == 100)
                            fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " fails to recover.;";
                    }
                }

                if (stageAction)
                {
                    // :TODO implement
//                    if (moveSucces)
//                    {
//                        if ($addToDefenseStage != 0)
//                            modifyStage("defense",$addToDefenseStage, $selfStageAction, $openentStageAction, effectSucces, attackFightPlayer, defendingFightPlayer);
//                        if ($addToAttackStage != 0)
//                            modifyStage("attack",$addToAttackStage, $selfStageAction, $openentStageAction, effectSucces, attackFightPlayer, defendingFightPlayer);
//                        if ($addToSpDefenseStage != 0)
//                            modifyStage("spDefense",$addToSpDefenseStage, $selfStageAction, $openentStageAction, effectSucces, attackFightPlayer, defendingFightPlayer);
//                        if ($addToSpAttackStage != 0)
//                            modifyStage("spAttack",$addToSpAttackStage, $selfStageAction, $openentStageAction, effectSucces, attackFightPlayer, defendingFightPlayer);
//                        if ($addToSpeedStage != 0)
//                            modifyStage("speed",$addToSpeedStage, $selfStageAction, $openentStageAction, effectSucces, attackFightPlayer, defendingFightPlayer);
//                    }
                }

            }

            // bericht bij falen van effect
            else if (effectAction && !moveSucces)
            {
                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " fails to perform move.;";
            }

            // Flinch
            if (flinch && firstMove)
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
            if (paralysisActionSucces)
            {
                if (random.nextInt(4) == 1)
                {
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is paralyzed. It cant move.;";
                    defendingFightPlayer.move = null;
                }
            }
            if (confusionActionSucces)
            {
                if (random.nextInt(2) == 1)
                {
                    fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is confused.;";
                    defendingFightPlayer.move = null;
                }
            }
            if (sleepActionSucces)
            {
                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is a sleep.;";
                defendingFightPlayer.move = null;
            }
            if (freezeActionSucces)
            {
                fight.log += "m:" + defendingOwnerPokemon.pokemon.name + " is frozen solid!;";
                defendingFightPlayer.move= null;
            }
        }
    }



    void recover(Fight fight, int recover, FightPlayer attackFightPlayer)
    {
        // Voeg hp toe

        if (attackFightPlayer.hp + recover > attackFightPlayer.maxHp)
        {
            recover = attackFightPlayer.maxHp - attackFightPlayer.hp;
        }
        attackFightPlayer.hp = attackFightPlayer.hp + recover;

        // turn negative
        healthSlideLogAction(fight, attackFightPlayer,recover * -1);
        // log
        fight.log += "m:" + attackFightPlayer.ownerPokemon.pokemon.name + " recovers ${recover} hp.;";

    }

    Move choseWildMove(FightPlayer fightPlayer)
    {

        List<LearnableMove> learnableMoveList = LearnableMove.findAllByPokemonAndLearnLevelLessThanEquals(fightPlayer.ownerPokemon.pokemon,fightPlayer.level).findAll{ it.move.implemented }
        if (learnableMoveList){
            Collections.shuffle(learnableMoveList)
            return learnableMoveList.last().move
        }
//       return 394; // Geen move, struggle
    }


    void beforeChosingMove(Fight fight, FightPlayer fightPlayer, Owner owner)
    {

        // freeze
        checkFreeze(fight,fightPlayer);
//        // paralyses              :TODO implement
//        checkParalyses($mp);
//        // confusion
//        checkConfusion($mp);
//        // sleep
//        checkSleep($mp);

        // continue last move
        if (fightPlayer.lastMove?.id == 376 || fightPlayer.lastMove?.id == 360)
        {

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
