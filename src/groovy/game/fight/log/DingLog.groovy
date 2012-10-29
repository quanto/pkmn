package game.fight.log

import game.fight.log.BattleLog

class DingLog extends BattleLog {

    int level
    int playerNr

    public DingLog(int level, int playerNr){
        this.level = level
        this.playerNr = playerNr
    }
    //fight.log += "l:${ownfightPlayer.playerNr}:" + newLevelLoop + ";";
}
