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

class UseItem {

    public static void useItem(Fight fight, Owner itemOwner, OwnerItem ownerItem, FightPlayer attackingFightPlayer, FightPlayer defendingFightPlayer){

        Item item = ownerItem.item
        if (!item.implemented){
            fight.roundResult.personalActions.add(new MessageLog("You can not use item ${item.name} in battle."))
        }
        else {

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

                // Set an empty move so round can be done
                Moves.setMove(fight,attackingFightPlayer,null,false);

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

}
