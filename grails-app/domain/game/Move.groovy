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
        effect nullable: true
        tmHm nullable :true
    }

    static scaffoldList           = ['nr','name','type','category','implemented']
    static scaffoldSearch         = ['nr','name','type','category','implemented']
}
