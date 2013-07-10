package game

import game.item.Item

class RewardItem {

    Item item
    int quantity

    @Override
    public String toString(){
        return "Item: ${item.name}, Quantity: ${quantity}"
    }

}
