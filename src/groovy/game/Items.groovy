package game

import game.item.Item
import game.item.UsableItem

class Items {

    public static boolean addOwnerItem(Player player, Item item, boolean chargeCost){

        if (chargeCost){
            // Only usable items can be bought
            assert item in UsableItem
        }

        if (chargeCost && player.money - item.cost < 0)
        {
            return false
        }

        OwnerItem ownerItem = OwnerItem.findByOwnerAndItem(player,item)

        Player.withTransaction {

            if (chargeCost){
                player.money -= item.cost
            }

            if (!ownerItem){
                ownerItem = new OwnerItem(
                        owner: player,
                        item :item,
                        quantity:1
                )
                player.addToOwnerItems(ownerItem)
            }
            else {
                ownerItem.quantity += 1
            }

            player.save()
            ownerItem.save()
        }
    }

}
