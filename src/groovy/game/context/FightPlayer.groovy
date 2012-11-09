package game.context

import game.Owner


import game.Move
import game.fight.action.BattleAction
import game.fight.action.MoveAction

class FightPlayer {

    String name
    Fight fight
    Integer playerNr
    Integer ownerId
    PlayerType playerType

    FightPokemon fightPokemon

    List<FightPokemon> party = []


    BattleAction battleAction
    boolean doNoMove = false
    boolean faintMessageShown = false
    List<Move> learnMoves = []
    int holdMove
    int holdTurns
    Move continueMove = null
    BattleAction lastBattleAction = null
    boolean waitOnOpponentMove = false
    boolean leechSeed = false
    boolean endure = false
    boolean protect = false
    int protectAccuracy = 100
    MoveAction prepareMoveAction = null // Should bet rest manually
    boolean takePP = true
    boolean mustSwitch = false

    /**
     * Properties to reset after a battle
     */
    public void restAfterRound(){
        endure = false
        waitOnOpponentMove = false
        battleAction = null
        doNoMove = false
        protect = false
        faintMessageShown = false
        takePP = true
    }

    FightPlayer opponentFightPlayer(){

        if(fight.fightPlayer1 == this){
            return fight.fightPlayer2
        }
        else {
            return fight.fightPlayer1
        }

    }

    /**
     * When making use of this object
     * we merge to attatch the object
     * to the session again.
     * @return
     */
    public Owner getOwner(){
        if (!ownerId)
            return null

        return Owner.get(ownerId)
    }



}
