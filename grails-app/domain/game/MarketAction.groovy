package game

import game.context.ActionType

class MarketAction extends Action {

    ActionType actionType = ActionType.Server

    Market market

    static constraints = {
    }

}
