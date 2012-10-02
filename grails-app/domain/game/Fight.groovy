package game

class Fight {

    //int player1

    FightPlayer fightPlayer1
    FightPlayer fightPlayer2

    String log = ""

    BattleType battleType
    boolean battleOver

    static constraints = {
        fightPlayer2 nullable: true
    }

    FightPlayer myPlayer(Owner owner){
        if(owner == fightPlayer1.owner){
            return fightPlayer1
        }
        else if(owner == fightPlayer2.owner){
            return fightPlayer2
        }
    }

    FightPlayer opponentPlayer(Owner owner){
        if (myPlayer(owner)){
            if(owner == fightPlayer1.owner){
                return fightPlayer1
            }
            else  if(owner == fightPlayer2.owner){
                return fightPlayer2
            }
        }
    }
}
