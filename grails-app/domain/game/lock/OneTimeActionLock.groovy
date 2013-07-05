package game.lock

import game.action.Action

class OneTimeActionLock extends Lock {

    Action action
    boolean permanent = true

    static constraints = {
    }
}
