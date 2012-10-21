package game

import map.View

class MarketController {

    def index() {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        MarketAction marketAction = MarketAction.findByPositionXAndPositionY(player.positionX, player.positionY)

        if (player.view != View.ShowMarket || !marketAction){
            render text : "Zit niet in een PokeMarket"
        }
        else {
            render text: g.render(template: 'market', model: [money:player.money,ownerItems:player.ownerItems,marketItems:marketAction.market.marketItems])
        }
    }

    def exit = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        MarketAction marketAction = MarketAction.findByPositionXAndPositionY(player.positionX, player.positionY)

        if (player.view != View.ShowMarket || !marketAction){
            render text : "Zit niet in een PokeMarket"
        }
        else {
            player.view = View.ShowMap
            player.save()
            redirect controller: 'game', action:'index'
        }
    }

    def buy = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        MarketAction marketAction = MarketAction.findByPositionXAndPositionY(player.positionX, player.positionY)

        if (player.view != View.ShowMarket || !marketAction){
            render text : "Zit niet in een PokeMarket"
        }
        else {

            int itemId = Integer.parseInt(params.id)

            MarketItem marketItem = marketAction.market.marketItems.find{ MarketItem marketItem -> marketItem.item.id == itemId }
            Item item = marketItem?.item

            if (item){

                if (player.money - item.cost >= 0)
                {
                    OwnerItem ownerItem = OwnerItem.findByOwnerAndItem(player,item)

                    Player.withTransaction {

                        player.money -= item.cost
                        if (!ownerItem){
                            ownerItem = new OwnerItem(
                                    owner: player,
                                    item :item,
                                    quantity:1
                            )
                        }
                        else {
                            ownerItem.quantity += 1
                        }
                        player.save()
                        ownerItem.save()
                    }
                }
            }
        }

        redirect controller: 'game', action:'index'
    }

    /**
     * function buy($itemId)
     {
     global $marktId, $owner;

     $item = new items();
     $item->id = $itemId;
     $item->get();

     $sql = "select id from marktitems where itemId = '$itemId' AND marktId = '$marktId'";
     if (mysql_num_rows(DatabaseQuery::Execute($sql)) != 1)
     {
     die ("Item bestaat niet in shop");
     }

     if ($owner->money - $item->cost >= 0)
     {
     $owner->money -= $item->cost;

     $sql = "select quantity from owneritem where itemId = '$itemId' AND ownerId = '" . $owner->id . "'";
     $result = DatabaseQuery::Execute($sql);
     if (mysql_num_rows($result) > 0)
     {
     $row = mysql_fetch_row($result);

     $sql = "update owneritem set quantity = '" . ($row[0] + 1) . "' where itemId = '$itemId' AND ownerId = '" . $owner->id . "'";
     DatabaseQuery::Execute($sql);
     }
     else
     {
     $ownerItem = new ownerItem();
     $ownerItem->ownerId = $owner->id;
     $ownerItem->itemId = $itemId;
     $ownerItem->quantity = 1;
     $ownerItem->insert();
     }

     $owner->update();
     }
     else
     {
     die("Not enough money");
     }

     header("Location: markt.php");
     }
     */

}
