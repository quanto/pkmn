package game

class Item {

    String name
    String effect
    int cost
    boolean implemented
    String image

    static constraints = {
    }

    @Override
    public String toString(){
        return "${name} Cost: ${cost} Implemented: ${implemented}"
    }

}
