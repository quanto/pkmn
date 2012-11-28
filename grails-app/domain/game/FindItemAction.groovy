package game

import game.context.ActionType

class FindItemAction extends Action {

    ActionType actionType = ActionType.Mixed

    String actionFunction = "findItem"
    String tileImage = "53"

    static hasMany = [rewardItems: OwnerItem]
}
