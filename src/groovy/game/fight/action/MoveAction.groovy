package game.fight.action

import game.Move
import game.fight.FightMove

class MoveAction extends BattleAction {
    FightMove ownerMoveForPP // Which move should we take pp from
    Move move // Which move should we perform
}
