package game

class OwnerItem {

    Item item
    int quantity

    static belongsTo = [owner: Owner]

    static constraints = {
    }

    @Override
    public String toString(){
        return "Item: ${item.name}, Quantity: ${quantity}"
    }

}
