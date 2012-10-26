package game.fight

class DingAction extends BattleAction {

    int level
    int playerNr

    public DingAction(int level, int playerNr){
        this.level = level
        this.playerNr = playerNr
    }
    //fight.log += "l:${ownfightPlayer.playerNr}:" + newLevelLoop + ";";
}
