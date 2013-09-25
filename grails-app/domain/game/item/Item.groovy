package game.item

class Item {

    boolean hidden = false
    String name
    String image

    static constraints = {
        image nullable: true
    }

}
