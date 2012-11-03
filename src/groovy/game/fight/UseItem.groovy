package game.fight

import game.items.PokeBall
import game.fight.log.MessageLog
import game.context.FightPlayer
import game.context.Fight
import game.Owner
import game.OwnerItem
import game.Item
import game.Moves
import game.items.RecoverItem
import game.fight.action.NoAction
import game.fight.action.ItemAction
import game.UsableItem
import game.PokeBallItem
import game.context.BattleType

class UseItem {

    public static void setItemAction(Fight fight, FightPlayer attackingFightPlayer, OwnerItem ownerItem){

        Item item = ownerItem.item

        assert item in UsableItem

        if (!item.implemented){
            fight.roundResult.personalActions.add(new MessageLog("You can not use item ${item.name} in battle."))
        }
        else if (item in PokeBallItem && fight.battleType != BattleType.PVE){
            fight.roundResult.personalActions.add(new MessageLog("PokeBalls can only be used in wild battles."))
        }
        else {
            // Set the item action
            Moves.setMove(fight,attackingFightPlayer,new ItemAction(ownerItem: ownerItem),false)
        }
    }

    public static void useItem(Fight fight, OwnerItem ownerItem, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer){

        Item item = ownerItem.item
        Owner itemOwner = attackingFightPlayer.owner

        fight.roundResult.battleActions.add(new MessageLog("${itemOwner.name} uses ${item.name}."))

        // Try a PokeBall
        Double catchFactor = PokeBall.throwBall(fight, itemOwner, item, attackingFightPlayer, defendingFightPlayer)
        boolean throwBall = catchFactor > 0

        if (throwBall){
            PokeBall.catchSuccess(fight, itemOwner, item, attackingFightPlayer, defendingFightPlayer, catchFactor)
        }

        // Try a potion
        boolean recoverItemUsed = RecoverItem.useRecoverItem(fight, item, attackingFightPlayer, defendingFightPlayer)

        if (throwBall || recoverItemUsed){

            if (ownerItem.quantity <= 1){
                itemOwner.removeFromOwnerItems(ownerItem)
                ownerItem.delete()
            }
            else {
                ownerItem.quantity -= 1
                ownerItem.save()
            }

        }
        else {
            println "ERROR: ${item.name} not supported"
        }
    }

}
