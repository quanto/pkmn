package game.fight

class MoveAction extends BattleAction {

    int damage
    int playerNr


    public MoveAction(int damage, int playerNr){
        this.damage = damage
        this.playerNr = playerNr
    }
    //
}
