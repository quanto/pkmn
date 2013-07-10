package game

import game.item.Item

class OwnerItem {

    Item item
    int quantity

    static belongsTo = [owner: Owner]

    static constraints = {
        owner nullable: true
    }

    @Override
    public String toString(){
        return "Item: ${item.name}, Quantity: ${quantity}"
    }

}
