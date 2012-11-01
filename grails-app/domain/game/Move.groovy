package game

class Move {

    int nr
    String name
    String type
    String category
    int power
    int accuracy
    int pp
    Integer tmHm = null
    String effect
    int effectProb
    int implemented
    int priority

    static constraints = {
        tmHm nullable :true
    }
}
