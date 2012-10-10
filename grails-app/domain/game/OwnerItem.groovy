package game

class OwnerItem {

    Item item
    int quantity

    static belongsTo = [owner: Owner]

    static constraints = {
    }
}
