package game

class Item {

    String name
    String image

    static constraints = {
    }

    @Override
    public String toString(){
        return "${name} Cost: ${cost} Implemented: ${implemented}"
    }

}
