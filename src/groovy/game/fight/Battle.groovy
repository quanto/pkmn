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
import game.context.MoveCategory
import game.fight.action.FailAction
import game.OwnerMove

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
        // Reset the switch round
        fight.switchRound = false

        // Log initial hp
        fight.roundResult.initialActions.add(new InitialHpLog(fightPlayer1.hp, fightPlayer1.ownerPokemon,1))
        fight.roundResult.initialActions.add(new InitialHpLog(fightPlayer2.hp, fightPlayer2.ownerPokemon,2))

        decideAction(fight, fight.getFirstFightPlayer(), fight.getSecondFightPlayer(), true)

        boolean fainted = Faint.oneOfBothFainted(fight)

        if (!fainted){
            decideAction(fight, fight.getSecondFightPlayer(), fight.getFirstFightPlayer(), false)

            Faint.oneOfBothFainted(fight)
        }

        if (fight.getFirstFightPlayer().hp > 0){
            afterBattle(fight,fight.getFirstFightPlayer(),fight.getSecondFightPlayer())
        }
        if (fight.getSecondFightPlayer().hp > 0){
            afterBattle(fight,fight.getSecondFightPlayer(),fight.getFirstFightPlayer())
        }
        // Check again after the affterBattle effects
        Faint.checkEndRoundFainted(fight)

        afterTurn(fight)
    }

    public static void takePP(OwnerMove ownerMove){
        // No ownerMove means there's no move to take pp from
        if (ownerMove){
            ownerMove.ppLeft -= 1
            ownerMove.save()
        }
    }

    public static FightPlayer decideAction(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer, boolean firstMove){

        if (attackFightPlayer.battleAction in MoveAction){
            attack(fight,attackFightPlayer, defendingFightPlayer, firstMove)

            if(attackFightPlayer.takePP){
                takePP(attackFightPlayer.battleAction.ownerMoveForPP)
            }
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
        else if (attackFightPlayer.battleAction in FailAction){
            fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " fails to perform move."))
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

        fight.fightPlayer1.restAfterRound()
        fight.fightPlayer2.restAfterRound()
    }

    static void afterBattle(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer)
    {

        if (defendingFightPlayer.leechSeed){
            // 1/8ste schade
            int seedDamage = Math.floor(defendingFightPlayer.maxHp / 8);
            // Less than 16 hp means 1 damage
            if (defendingFightPlayer.hp < 16){
                seedDamage = 1
            }

            seedDamage = Hp.doStatusDamage(defendingFightPlayer,seedDamage)
            Hp.doRecover(attackFightPlayer, seedDamage)

            fight.roundResult.battleActions.add(new MessageLog("Leech Seed saps " + defendingFightPlayer.ownerPokemon.pokemon.name + ""))

            Recover.healthSlideLogAction(fight, attackFightPlayer,seedDamage* -1);
            Recover.healthSlideLogAction(fight, defendingFightPlayer,seedDamage);
        }

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

            Hp.doStatusDamage(attackFightPlayer,burnDamage)

            fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " is hurt by its burn burnDamage."))

            Recover.healthSlideLogAction(fight, attackFightPlayer,burnDamage);
        }

        // poison wanneer dit niet is gezet
        if (attackFightPlayer.poison == 1)
        {
            // 1/8ste schade
            int poisonDamage = Math.floor(attackFightPlayer.maxHp / 8);
            Hp.doStatusDamage(attackFightPlayer,poisonDamage)
            fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " hurts from poison poisonDamage."))
            Recover.healthSlideLogAction(fight, attackFightPlayer,poisonDamage);
        }

        // badly poisond
        if (attackFightPlayer.badlypoisond > 0)
        {
            // steeds verhoogt met 1/16
            int badlyPoisonDamage = Math.floor(attackFightPlayer.maxHp / 16 * attackFightPlayer.badlypoisond);

            Hp.doStatusDamage(attackFightPlayer,badlyPoisonDamage)
            fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " hurts from badly poison badlyPoisonDamage."))
            Recover.healthSlideLogAction(fight, attackFightPlayer,badlyPoisonDamage);
            // verhoog status
            attackFightPlayer.badlypoisond = attackFightPlayer.badlypoisond + 1;

        }
    }


    public static void attack(Fight fight, FightPlayer attackFightPlayer, FightPlayer defendingFightPlayer, boolean firstMove)
    {

        assert attackFightPlayer.battleAction in MoveAction
        assert attackFightPlayer.battleAction.move

        Move attackMove = attackFightPlayer.battleAction.move

        MoveInfo moveInfo = new MoveInfo()

        // Set the values on an move info object so we can alter them
        moveInfo.accuracy = attackMove.accuracy
        moveInfo.attackPower = attackMove.power

        OwnerPokemon attackOwnerPokemon = attackFightPlayer.ownerPokemon
        OwnerPokemon defendingOwnerPokemon = defendingFightPlayer.ownerPokemon

        Random random = new Random()

        moveInfo.criticalHitStage += attackFightPlayer.criticalStage;


        fight.roundResult.battleActions.add(new MessageLog(attackFightPlayer.ownerPokemon.pokemon.name + " uses " + attackMove.name + "."))

        if (attackMove.category == MoveCategory.PhysicalMove || attackMove.category == MoveCategory.SpecialMove)
        {
            FightMove.getMoveInfo(moveInfo, attackFightPlayer.battleAction, fight, attackFightPlayer, defendingFightPlayer, firstMove)
        }
        else if (attackMove.category == MoveCategory.StatusMove)
        {
            StatusMove.getMoveInfo(moveInfo, attackMove, fight, attackFightPlayer, defendingFightPlayer)
        }

        // openent moves die ervoor zogen dat er niet gemist kan worden
        if (defendingFightPlayer.battleAction in MoveAction && defendingFightPlayer.battleAction.move.name == "Solarbeam")
        {
            moveInfo.cantMiss = true
        }

        // bereken accuracy
        double accuracy = Accuraccy.getAccuracy(moveInfo.accuracy,attackFightPlayer.accuracyStage);

        // evade over accuracy
        int chanceOnHitting = Evasion.getEvasion(accuracy,defendingFightPlayer.evasionStage);

        boolean moveSucces = false;
        if (random.nextInt(100)+1 <= chanceOnHitting || moveInfo.cantMiss)
        {
            moveSucces = true;
        }

        if (defendingFightPlayer.prepareMoveAction && (defendingFightPlayer.prepareMoveAction.move.name == "Fly" || defendingFightPlayer.prepareMoveAction.move.name == "Dig")){
            // You can not hit on fly or dig
            moveSucces = false
        }

        if (moveInfo.doPhysicalDamage)
        {
            if (moveSucces){
                PhysicalDamage.doDamage(fight, moveInfo, attackFightPlayer, defendingFightPlayer, attackMove)
            }
            else {
                // No message if we can not use the action. message was set elsewhere
                if (!moveInfo.canNotUseAction)
                {
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " avoided the attack."))
                }
            }
        }

        // Kijk of een aanval een status opheft
        if (attackMove.type == "fire" && defendingFightPlayer.freeze == 1)
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
        if (attackMove.effectProb == 0 || random.nextInt(100)+1 <= attackMove.effectProb)
        {
            if (moveInfo.effectAction && attackMove.category == MoveCategory.StatusMove)
            {
                // Protect cancels the effect
                if (moveInfo.effectSucces){
                    fight.roundResult.battleActions.add(new MessageLog("${attackFightPlayer.ownerPokemon.pokemon.name} protected itself."))
                }
                else {
                    moveSucces = true
                    moveInfo.effectSucces = true
                }
            }
            else if (moveSucces)
            {
                moveInfo.effectSucces = true
            }
        }



        // Apply effect
        if ((moveInfo.effectAction && moveSucces))
        {
            // Leech seed
            if (attackMove.name == "Leech Seed"){
                if (defendingOwnerPokemon.pokemon.hasType("grass")){
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " cannot be seeded."))
                }
                else if (defendingFightPlayer.leechSeed){
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " is already seeded."))
                }
                else {
                    defendingFightPlayer.leechSeed = true
                    fight.roundResult.battleActions.add(new MessageLog(defendingOwnerPokemon.pokemon.name + " was seeded."))
                }
            }

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

            Paralyses.paralysisAction(fight, moveInfo, attackFightPlayer, defendingFightPlayer, attackMove)

            Burn.burnAction(fight, moveInfo, defendingFightPlayer, attackMove)

            Freeze.freezeAction(fight, moveInfo, defendingFightPlayer, attackMove)

            Poison.poisonAction(fight, moveInfo, defendingFightPlayer, attackMove)

            Poison.badlyPoisondAction(fight, moveInfo, defendingFightPlayer, attackMove)

            if (moveInfo.recoverAction)
            {
                if (moveSucces && moveInfo.effectSucces)
                {
                    Recover.recover(fight,moveInfo.recover, attackFightPlayer);
                }
                else
                {
                    if (attackMove.effectProb == 0 || attackMove.effectProb == 100)
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
            fight.roundResult.battleActions.add(new MessageLog(attackOwnerPokemon.pokemon.name + " fails to perform move."))
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
