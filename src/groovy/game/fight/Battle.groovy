package game.fight

import game.fight.effect.Poison
import game.fight.effect.Burn

import game.fight.log.MessageLog
import game.fight.effect.Paralyses
import game.fight.effect.Freeze
import game.fight.status.Recover
import game.fight.effect.Sleep
import game.fight.status.Stats
import game.fight.status.Accuraccy
import game.fight.status.Evasion
import game.fight.effect.Confusion
import game.fight.status.Stage
import game.fight.status.Stat
import game.fight.calculation.Damage
import game.fight.calculation.CriticalHit
import game.fight.calculation.Effective
import game.context.FightPlayer
import game.context.MoveInfo
import game.context.PlayerType
import game.fight.reward.EXP
import game.fight.reward.Money

import game.fight.calculation.BattleOrder
import game.context.BattleType
import game.context.Fight
import game.OwnerPokemon
import game.Player
import game.RecoverAction
import game.Npc
import game.OwnerItem
import game.Items
import game.NpcLock
import game.Move

import game.Owner
import game.fight.action.MoveAction
import game.fight.action.SwitchAction
import game.fight.action.NoAction
import game.fight.log.InitialHpLog
import game.fight.action.ItemAction

class Battle {

    /**
     * If both player moves are set we can do a round.
     * @param fight
     */
    public static void battle(Fight fight)
    {
        fight.player1first = BattleOrder.player1First(fight)
        FightPlayer fightPlayer1 = fight.fightPlayer1
        FightPlayer fightPlayer2 = fight.fightPlayer2

        // New round, start with a new object
        fight.roundResult = new RoundResult()

        // Log initial hp
        fight.roundResult.initialActions.add(new InitialHpLog(fightPlayer1.hp, fightPlayer1.ownerPokemon,1))
        fight.roundResult.initialActions.add(new InitialHpLog(fightPlayer2.hp, fightPlayer2.ownerPokemon,2))

        decideAction(fight, fight.getFirstFightPlayer(), fight.getSecondFightPlayer(), true)

        checkPokemonFainted(fight)

        if (!fight.battleOver){
            decideAction(fight, fight.getSecondFightPlayer(), fight.getFirstFightPlayer(), false)

            checkPokemonFainted(fight)
        }

        if (!fight.battleOver){
            afterBattle(fight,fight.getFirstFightPlayer(),fight.getSecondFightPlayer())
            afterBattle(fight,fight.getSecondFightPlayer(),fight.getFirstFightPlayer())

            // Check again after the affterBattle effects
            checkPokemonFainted(fight)
        }

        afterTurn(fight)
    }

    public static FightPlayer decideAction(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer, boolean firstMove){

        if (attackFightPlayer.battleAction in MoveAction){
            attack(fight,attackFightPlayer, defendingFightPlayer, firstMove)
        }
        else if (attackFightPlayer.battleAction in SwitchAction){
            // Save the old pokemon
            Stats.saveStats(attackFightPlayer, false)
            // Bring out the new
            Stats.setBaseStats(fight,attackFightPlayer.battleAction.ownerPokemon.refresh(), attackFightPlayer.playerType, attackFightPlayer.playerNr)
        }
        else if (attackFightPlayer.battleAction in NoAction){
            // We do nothing at all
        }
        else if (attackFightPlayer.battleAction in ItemAction){
            UseItem.useItem(fight, attackFightPlayer.battleAction.ownerItem, attackFightPlayer, defendingFightPlayer)
        }
        else {
            println attackFightPlayer.battleAction
            assert false
        }
        return attackFightPlayer
    }

    /**
     * We should check after the first move if this prevents the second move
     * @param fight
     * @param attackFightPlayer
     * @param defendingFightPlayer
     */
    public static void afterFirstMove(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer, MoveInfo moveInfo, Move attackMove){
        Random random = new Random()

        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        // Flinch
        if (moveInfo.flinch)
        {
            defendingFightPlayer.battleAction = new NoAction()
            fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " flinched!"))
        }

        // holding
        if (attackFightPlayer.holdMove != 0)
        {
            fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is being traped by " + attackMove.name + "."))
            defendingFightPlayer.battleAction = new NoAction()
        }

        // kijk of de volgende actie faalt
        if (moveInfo.paralysisActionSucces)
        {
            if (random.nextInt(4) == 1)
            {

                fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is paralyzed. It cant move."))
                defendingFightPlayer.battleAction = new NoAction()
            }
        }
        if (moveInfo.confusionActionSucces)
        {
            if (random.nextInt(2) == 1)
            {

                fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is confused."))
                defendingFightPlayer.battleAction = new NoAction()
            }
        }
        if (moveInfo.sleepActionSucces)
        {
            fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is a sleep."))
            defendingFightPlayer.battleAction = new NoAction()
        }
        if (moveInfo.freezeActionSucces)
        {
            fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is frozen solid!"))
            defendingFightPlayer.battleAction = new NoAction()
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
        fight.fightPlayer1.lastBattleAction = fight.fightPlayer1.battleAction
        fight.fightPlayer2.lastBattleAction = fight.fightPlayer2.battleAction

        // zet moves weer op 0
        fight.fightPlayer1.battleAction = null
        fight.fightPlayer2.battleAction = null

        fight.fightPlayer1.doNoMove = false
        fight.fightPlayer2.doNoMove = false

        fight.fightPlayer1.waitOnOpponentMove = false
        fight.fightPlayer2.waitOnOpponentMove = false
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
            fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " is hurt by its burn burnDamage."))

            Recover.healthSlideLogAction(fight, attackFightPlayer,burnDamage);
        }

        // poison wanneer dit niet is gezet
        if (attackFightPlayer.poison == 1)
        {
            // 1/8ste schade
            int poisonDamage = Math.floor(attackFightPlayer.maxHp / 8);
            attackFightPlayer.hp = attackFightPlayer.hp - poisonDamage;
            fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " hurts from poison poisonDamage."))
            Recover.healthSlideLogAction(fight, attackFightPlayer,poisonDamage);
        }

        // badly poisond
        if (attackFightPlayer.badlypoisond > 0)
        {
            // steeds verhoogt met 1/16
            int badlyPoisonDamage = Math.floor(attackFightPlayer.maxHp / 16 * attackFightPlayer.badlypoisond);
            attackFightPlayer.hp = attackFightPlayer.hp - badlyPoisonDamage;

            fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " hurts from badly poison badlyPoisonDamage."))
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

            fight.roundResult.battleActions.add(new MessageLog(fightPlayer.ownerPokemon.pokemon.name + " fainted."))
            // stop attacks
            fight.fightPlayer1.battleAction = null
            fight.fightPlayer2.battleAction = null

            return true;
        }
        return false;
    }

    static void win(Fight fight, boolean giveEXP = true)
    {
        // battle is over speler 1 wint

        fight.roundResult.battleActions.add(new MessageLog(fight.fightPlayer1.owner.name + " wins."))

        if(giveEXP){
            EXP.distributeExp(fight,fight.fightPlayer1,fight.fightPlayer2,true);

            if (fight.battleType == BattleType.PVE){
                int money = Money.calculateMoney(fight,fight.fightPlayer2.ownerPokemon)
                Money.giveMoney(fight,money)
            }
            else if (fight.battleType == BattleType.PVN){
                int money = 0
                OwnerPokemon.findAllByOwner(fight.fightPlayer2.owner).each {
                    money += Money.calculateMoney(fight,it)
                }
                Money.giveMoney(fight,money)
            }
        }

        Stats.saveStats(fight.fightPlayer1, true);
        fight.battleOver = true
    }


    static void handlePvPFainted(Fight fight){
        boolean player1fainted = checkFainted(fight,fight.fightPlayer1)
        boolean player2fainted = checkFainted(fight,fight.fightPlayer2)

        // draw if the other has no alive pokemon
        boolean player1Alive = player1fainted?hasAlivePokemon(fight.fightPlayer1):true
        boolean player2Alive = player2fainted?hasAlivePokemon(fight.fightPlayer2):true

        // Is the battle over
        if (!player1Alive || !player2Alive){
            // whats the result
            if (!player1Alive && !player2Alive){
                // draw
            }
            else if (!player1Alive){
                // :TODO implement
            }
            else {

            }

            // Recover and end
            Recover.recoverParty(fight.fightPlayer1.owner)
            Recover.recoverParty(fight.fightPlayer2.owner)

            fight.battleOver = true
        }
    }

    static boolean hasAlivePokemon(FightPlayer fightPlayer){
        // update pokemon hp
        fightPlayer.ownerPokemon.hp = 0
        fightPlayer.ownerPokemon.save(flush: true)

        // kijk of er nog levende pokemon zijn
        def list = OwnerPokemon.findAllByOwnerAndPartyPositionGreaterThanAndHpGreaterThan(fightPlayer.owner,0,0)

        return list.size() > 0
    }

    static void checkPokemonFainted(Fight fight)
    {
        if (fight.battleType == BattleType.PVP){
            handlePvPFainted(fight)
            return
        }

        boolean player1fainted = checkFainted(fight,fight.fightPlayer1)
        boolean player2fainted = checkFainted(fight,fight.fightPlayer2)

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


            if (hasAlivePokemon(fight.fightPlayer1))
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


                fight.roundResult.battleActions.add(new MessageLog("You lose, your pokemon have been recovered in town."))
                Recover.recoverParty(fight.fightPlayer1.owner)
                fight.battleOver = true

            }
        }

    }

    static public boolean checkNpcLoses(Fight fight)
    {

        FightPlayer fightPlayer1 = fight.fightPlayer1
        Npc npc = fight.fightPlayer2.owner

        // geef xp voor verslaan
        EXP.distributeExp(fight,fightPlayer1,fight.fightPlayer2,false);

        // haal volgende op als deze bestaat
        OwnerPokemon nextOwnerPokemon = OwnerPokemon.findByOwnerAndPartyPosition(fight.fightPlayer2.owner,fight.fightPlayer2.ownerPokemon.partyPosition + 1)

        if (!nextOwnerPokemon)
        {
            // Gevechts telling
            fightPlayer1.owner.pvnBattlesWon += 1;

            fight.roundResult.battleActions.add(new MessageLog("Defeated NPC!"))

            // Don't give exp. It was already given.
            win(fight, false)

            // Turn out Npc reward items
            npc.rewardItems.each { OwnerItem ownerItem ->
                Items.addOwnerItem(fightPlayer1.owner,ownerItem.item,false)
            }

            // Show defeated message
            if (npc.npcDefeatedMessage){
                npc.npcDefeatedMessage.split(';').each{
                    if (it){
                        fight.roundResult.battleActions.add(new MessageLog(it))
                    }
                }
            }

            // Create an NPC lock
            new NpcLock(
                    player: fightPlayer1.owner,
                    npc : fight.fightPlayer2.owner,
                    permanent: fight.fightPlayer2.owner.permanentLock
            ).save()

            return true
        }
        else
        {
            // Log de switch
            fight.roundResult.battleActions.add(new MessageLog("NPC brings out " + nextOwnerPokemon.pokemon.name + "."))

            fight.fightPlayer2 = Stats.setBaseStats(fight,nextOwnerPokemon, PlayerType.npc, 2);

            return false
        }
    }

    public static void attack(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer, boolean firstMove)
    {

        assert attackFightPlayer.battleAction in MoveAction
        assert attackFightPlayer.battleAction.move

        MoveInfo moveInfo = new MoveInfo()

        Move attackMove = attackFightPlayer.battleAction.move
        OwnerPokemon attackOwnerPokemon = attackFightPlayer.ownerPokemon
        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        Random random = new Random()

        String attackMovetype
        String attackMovecategory


        moveInfo.criticalHitStage += attackFightPlayer.criticalStage;


        fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " uses " + attackMove.name + "."))

        if (attackMove.category == "physical move" || attackMove.category == "special move")
        {

            moveInfo.effectAction = false

            moveInfo.poisonAction = false
            moveInfo.badlypoisondAction = false
            moveInfo.stageAction = false

            FightMove.getMoveInfo(moveInfo, attackMove, fight, attackFightPlayer, defendingFightPlayer)
        }
        else if (attackMove.category == "status move")
        {
            moveInfo.damageAction = false
            moveInfo.recoverAction = false

            StatusMove.getMoveInfo(moveInfo, attackMove, fight, attackFightPlayer, defendingFightPlayer)
        }

        // openent moves die ervoor zogen dat er niet gemist kan worden
        if (attackMove?.name == "Solarbeam")
        {
            moveInfo.cantMiss = true
        }

        // bereken accuracy
        double accuracy = Accuraccy.getAccuracy(attackMove.accuracy,attackFightPlayer.accuracyStage);

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
                        defendingFightPlayer.hp = 0

                        fight.roundResult.battleActions.add(new MessageLog("It's a one hit KO!"))
                    }

                    if (!moveInfo.oneHitKO)
                    {
                        // Critcal hits
                        if (CriticalHit.tryCriticalHit(moveInfo.criticalHitStage))
                        {
                            moveInfo.damage = moveInfo.damage * 2;
                            fight.roundResult.battleActions.add(new MessageLog("Critical hit!"))
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
                                fight.roundResult.battleActions.add(new MessageLog("It has no effect."))
                            else if (moveInfo.effectiveness == 0.25 || moveInfo.effectiveness == 0.5)
                                fight.roundResult.battleActions.add(new MessageLog("It`s not very effective."))
                            else if (moveInfo.effectiveness == 2 || moveInfo.effectiveness == 4)
                                fight.roundResult.battleActions.add(new MessageLog("It`s super effective."))
                        }

                        // Doe schade
                        defendingFightPlayer.hp = defendingFightPlayer.hp - moveInfo.damage;

                        fight.roundResult.battleActions.add(new MessageLog(attackOwnerPokemon.pokemon.name + " hits " + defendingOwnerPokemon.pokemon.name + " with " + attackMove.name + " ${moveInfo.damage} dmg."))
                        Recover.healthSlideLogAction(fight, defendingFightPlayer,moveInfo.damage);

                        if (moveInfo.recoil)
                        {
                            moveInfo.damage = Math.floor(moveInfo.damage / 100 * moveInfo.recoil);
                            attackFightPlayer.hp = Math.round(attackFightPlayer.hp - moveInfo.damage);

                            fight.roundResult.battleActions.add(new MessageLog(attackOwnerPokemon.pokemon.name + " hurts from recoil damage. ${moveInfo.damage} dmg."))
                            Recover.healthSlideLogAction(fight, attackFightPlayer,moveInfo.damage);
                            fight.roundResult.battleActions.add(new MessageLog("Recoil did ${moveInfo.damage} dmg."))
                        }

                        // Bericht bij loop
                        if (moveInfo.loop > 1 && moveInfo.loop == (i + 1))
                        {
                            fight.roundResult.battleActions.add(new MessageLog("Hits " + (i + 1) + " times"))
                        }

                    }
                }
                else if (moveInfo.canNotUseAction)
                {

                }
                else
                {
                    // Gemist
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " avoided the attack."))
                }

            }

        }

        // Kijk of een aanval een status opheft
        if (attackMovetype == "fire" && defendingFightPlayer.freeze == 1)
        {
            defendingFightPlayer.freeze == 0
            fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is no longer frozen."))
        }

        // set hold move
        if (moveInfo.holdMove && moveSucces)
        {
            // bij 0 kan de move gezet worden
            if (attackFightPlayer.holdTurns == 0)
            {
                attackFightPlayer.holdMove = attackFightPlayer.battleAction.move;
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
            Sleep.sleepAction(fight, moveInfo, attackFightPlayer, defendingFightPlayer)

            Confusion.confusionAction(fight, moveInfo, attackFightPlayer, defendingFightPlayer)

            Paralyses.paralysisAction(fight, moveInfo, attackFightPlayer, defendingFightPlayer)

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
                        fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " fails to recover."))
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
            fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " fails to perform move."))
        }

        if (firstMove){
            afterFirstMove(fight, attackFightPlayer, defendingFightPlayer, moveInfo, attackMove)
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
//        if (fightPlayer.lastMove?.id == 376 || fightPlayer.lastMove?.id == 360)
//        {
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
//        }


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
