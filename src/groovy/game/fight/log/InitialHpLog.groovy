package game.fight.log

/**
 * We set this at the start of a round to ensure the current hp is correct.
 */
class InitialHpLog extends BattleLog {

    int hp
    int playerNr

    public InitialHpLog(int hp, int playerNr){
        this.hp = hp
        this.playerNr = playerNr
    }

}
