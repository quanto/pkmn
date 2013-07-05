package game.action

import game.Market
import game.action.Action
import game.context.ActionType

class MarketAction extends Action {

    ActionType actionType = ActionType.Server

    Market market

    static constraints = {
    }

}
