package game.fight.action

import game.Move
import game.OwnerMove

class MoveAction extends BattleAction {
    OwnerMove ownerMoveForPP // Which move should we take pp from
    Move move // Which move should we perform
}
