package game.lock

import game.Player

class Lock {

    Player player
    Date dateCreated

    // Permanent locks are never released. Gym leaders for example.
    boolean permanent

    static constraints = {
    }

    static mapping = {
        table '`lock`'
    }

}
