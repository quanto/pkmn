package game.context

import game.Owner

import game.OwnerPokemon
import game.Move
import game.fight.action.BattleAction

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 13:37
 * To change this template use File | Settings | File Templates.
 */
class FightPlayer {

    Fight fight
    int playerNr
    Owner owner
    PlayerType playerType
    OwnerPokemon ownerPokemon
    int attack
    int defense
    int spAttack
    int spDefense
    int speed
    int maxHp
    int hp
    int level
    int burn
    int freeze
    int paralysis
    int poison
    int badlypoisond
    int sleep
    int confusion
    int curse
    int attackStage
    int defenseStage
    int spAttackStage
    int spDefenseStage
    int speedStage
    int accuracyStage
    int criticalStage
    int evasionStage
    BattleAction battleAction
    boolean doNoMove = false
    List<Move> learnMoves = []
    int holdMove
    int holdTurns
    Move continueMove
    BattleAction lastBattleAction
    boolean waitOnOpponentMove = false
    boolean leechSeed = false
    boolean endure = false

    /**
     * Properties to reset after a battle
     */
    public void restAfterRound(){
        endure = false
        waitOnOpponentMove = false
        battleAction = null
        doNoMove = false
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
        return getOwnerPokemon()?.owner
    }

    /**
     * When making use of this object
     * we merge to attatch the object
     * to the session again.
     * @return
     */
    public OwnerPokemon getOwnerPokemon(){
        return OwnerPokemon.get(ownerPokemon.id)
    }

}
