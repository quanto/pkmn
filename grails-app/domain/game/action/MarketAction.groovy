package game.action

import game.Market
import game.action.Action
import game.context.ActionType

class MarketAction extends Action {

    ActionType actionType = ActionType.Server
    boolean triggerOnActionButton = false
    boolean triggerBeforeStep = true

    Market market

    static constraints = {
    }

}
