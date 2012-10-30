package game.fight.log

/**
 * Used for the health slide
 */
class MoveLog extends BattleLog {

    int hp
    int playerNr


    public MoveLog(int hp, int playerNr){
        this.hp = hp
        this.playerNr = playerNr
    }

}
