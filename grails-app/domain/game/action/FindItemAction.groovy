package game.action

import game.RewardItem
import game.action.Action
import game.context.ActionType

class FindItemAction extends Action {

    ActionType actionType = ActionType.Mixed

    boolean placeOneTimeActionLock = true
    String actionFunction = "findItem"
    String image = "/images/tiles/sheet1/53.png"

    static hasMany = [rewardItems: RewardItem]
}
