package game.fight

import game.context.Fight
import game.fight.status.Recover
import game.context.BattleType
import game.Player
import game.action.RecoverAction
import game.fight.log.MessageLog
import game.context.FightPlayer
import game.Npc
import game.fight.reward.EXP
import game.OwnerPokemon
import game.OwnerItem
import game.Items
import game.lock.NpcLock

import game.fight.action.NoAction
import game.fight.reward.Money

class Faint {

    static boolean checkEndRoundFainted(Fight fight)
    {
        if (fight.battleType == BattleType.PVP){
            return handlePvPFainted(fight)
        }
        else if (fight.battleType == BattleType.PVE){
            return handlePveFainted(fight)
        }
        else if (fight.battleType == BattleType.PVN){
            return handlePvnFainted(fight)
        }
    }

    static boolean handlePvPFainted(Fight fight){
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
                fight.fightPlayer2.owner.pvpBattlesWon += 1
                fight.fightPlayer1.owner.pvpBattlesLost += 1
            }
            else {
                fight.fightPlayer1.owner.pvpBattlesWon += 1
                fight.fightPlayer2.owner.pvpBattlesLost += 1
            }

            // Recover and end
            Recover.recoverParty(fight.fightPlayer1.owner)
            Recover.recoverParty(fight.fightPlayer2.owner)

            fight.battleOver = true
            return true
        }
        else {
            // Should we do a switch round
            if (player1fainted || player2fainted){

                fight.switchRound = true
            }
            return true
        }
        return false
    }

    static boolean handlePveFainted(Fight fight){
        boolean player1fainted = checkFainted(fight,fight.fightPlayer1)
        boolean player2fainted = checkFainted(fight,fight.fightPlayer2)

        boolean player1Alive = player1fainted?hasAlivePokemon(fight.fightPlayer1):true

        if (player2fainted || !player1Alive){
            if (player2fainted && player1Alive){
                // win
                fight.fightPlayer1.owner.pveBattlesWon += 1;
                win(fight);

            }
            else if (!player2fainted && !player1Alive){
                fight.fightPlayer1.owner.pveBattlesLost += 1;
            }
            else {
                // draw
            }

            fight.battleOver = true
            return true
        }
        return false
    }

    static boolean handlePvnFainted(Fight fight){
        boolean player1fainted = checkFainted(fight,fight.fightPlayer1)
        boolean player2fainted = checkFainted(fight,fight.fightPlayer2)

        // draw if the other has no alive pokemon
        boolean player1Alive = player1fainted?hasAlivePokemon(fight.fightPlayer1):true
        boolean player2Alive = !player2fainted

        if (!player2Alive){
            // Try to find the next alive pokemon
            player2Alive = fight.fightPlayer2.party.find { it.hp > 0 }?true:false
        }

        // Is the battle over
        if (!player1Alive || !player2Alive){

            Npc npc = fight.fightPlayer2.owner

            // whats the result
            if (!player1Alive && !player2Alive){
                // draw
            }
            else if (!player1Alive){
                fight.fightPlayer1.owner.pvnBattlesLost += 1
            }
            else {
                fight.fightPlayer1.owner.pvnBattlesWon += 1

                win(fight);

                fight.roundResult.battleActions.add(new MessageLog("Defeated NPC!"))

                // Turn out Npc reward items
                npc.rewardItems.each { OwnerItem ownerItem ->
                    Items.addOwnerItem(fight.fightPlayer1.owner,ownerItem.item,false)
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
                        player: fight.fightPlayer1.owner,
                        npc : npc,
                        permanent: npc.permanentLock
                ).save()
            }

            fight.battleOver = true
            return true
        }
        else {
            if (player2fainted){
                EXP.distributeExp(fight,fight.fightPlayer1,fight.fightPlayer2,false);
            }

            // Should we do a switch round
            if (player1fainted || player2fainted){
                fight.switchRound = true
            }
            return true
        }
        return false
    }

    public static void recoverPlayerToClosestCenter(Fight fight){
        // Stuur speler terug naar laatste recover punt
        Player player = (Player)fight.fightPlayer1.owner
        RecoverAction recoverAction = player.lastRecoverAction

        player.map = recoverAction.map
        player.positionX = recoverAction.positionX
        player.positionY = recoverAction.positionY
        player.save()

        Recover.recoverParty(fight.fightPlayer1.owner)
        fight.roundResult.battleActions.add(new MessageLog("You lose, your pokemon have been recovered in town."))
    }

    static boolean hasAlivePokemon(FightPlayer fightPlayer){		
        // kijk of er nog levende pokemon zijn		
        def list = fightPlayer.party.findAll{ it.hp > 0 }

        return list.size() > 0
    }

    static boolean oneOfBothFainted(Fight fight){

        boolean fainted = false

        if (checkFainted(fight, fight.fightPlayer1)){
            fainted = true
        }
        if (checkFainted(fight, fight.fightPlayer2)){
            fainted = true
        }
        return fainted
    }

    static boolean checkFainted(Fight fight, FightPlayer fightPlayer)
    {
        if (fightPlayer.fightPokemon.hp <= 0)
        {
            fightPlayer.fightPokemon.hp = 0

            if (!fightPlayer.faintMessageShown){
                fight.roundResult.battleActions.add(new MessageLog(fightPlayer.fightPokemon.name + " fainted."))
            }

            // stop attacks
            fight.fightPlayer1.battleAction = new NoAction()
            fight.fightPlayer2.battleAction = new NoAction()

            return true;
        }
        return false;
    }

    static void win(Fight fight)
    {
        // battle is over speler 1 wint

        fight.roundResult.battleActions.add(new MessageLog(fight.fightPlayer1.owner.name + " wins."))

        EXP.distributeExp(fight,fight.fightPlayer1,fight.fightPlayer2,true);

        if (fight.battleType == BattleType.PVE){
            int money = Money.calculateMoney(fight,fight.fightPlayer2.fightPokemon.ownerPokemon)
            Money.giveMoney(fight,money)
        }
        else if (fight.battleType == BattleType.PVN){
            int money = 0
            OwnerPokemon.findAllByOwner(fight.fightPlayer2.owner).each {
                money += Money.calculateMoney(fight,it)
            }
            Money.giveMoney(fight,money)
        }

        fight.battleOver = true
    }

}
