package game.lock

import game.Action

class OneTimeActionLock extends Lock {

    Action action
    boolean permanent = true

    static constraints = {
    }
}
