package game

class FightPlayer {

    static belongsTo = [fight: Fight]

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
    Move move
    String learnMoves = ""
    int holdMove
    int holdTurns
    Move continueMove
    Move lastMove

    static constraints = {
        owner nullable:true
        move nullable: true
        continueMove nullable :true
        lastMove nullable : true
    }

    FightPlayer opponentFightPlayer(){

        if(fight.fightPlayer1 == this){
            return fight.fightPlayer2
        }
        else {
            return fight.fightPlayer1
        }

    }

}
