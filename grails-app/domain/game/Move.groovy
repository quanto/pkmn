package game

import game.context.MoveCategory

class Move {

    int nr
    String name
    String type
    MoveCategory category
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
