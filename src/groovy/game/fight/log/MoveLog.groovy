package game.fight.log

import game.fight.log.BattleLog

class MoveLog extends BattleLog {

    int damage
    int playerNr


    public MoveLog(int damage, int playerNr){
        this.damage = damage
        this.playerNr = playerNr
    }

}
