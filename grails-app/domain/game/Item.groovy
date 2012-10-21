package game

class Item {

    String name
    String effect
    int cost
    boolean implemented

    static constraints = {
    }

    @Override
    public String toString(){
        return "${name} Cost: ${cost} Implemented: ${implemented}"
    }

}
