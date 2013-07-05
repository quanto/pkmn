package game.action

import game.Npc
import game.action.Action
import game.context.ActionType

class NpcAction extends Action {

    ActionType actionType = ActionType.Server
    Npc owner

}
