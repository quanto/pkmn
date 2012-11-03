package game

class PokeBallItem extends UsableItem {

    @Override
    public String toString(){
        return "${name} Cost: ${cost} Implemented: ${implemented}"
    }

}
