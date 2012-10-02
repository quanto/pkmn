package game

class Move {

    String name
    String type
    String category
    int power
    int accuracy
    int pp
    Integer tmHm = null
    String effect
    String effectProb
    int implemented

    static constraints = {
        tmHm nullable :true
    }
}
