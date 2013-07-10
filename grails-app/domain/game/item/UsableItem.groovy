package game.item

class UsableItem extends Item {

    String effect
    int cost
    boolean implemented

    @Override
    public String toString(){
        return "${name} Cost: ${cost} Implemented: ${implemented}"
    }

}
