package game.action

import game.RewardItem
import game.action.Action
import game.context.ActionType

class FindItemAction extends Action {

    ActionType actionType = ActionType.Mixed

    boolean placeOneTimeActionLock = true
    String actionFunction = "findItem"
    String tileImage = "53"

    static hasMany = [rewardItems: RewardItem]
}