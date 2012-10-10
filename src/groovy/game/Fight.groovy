package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 03-10-12
 * Time: 13:36
 * To change this template use File | Settings | File Templates.
 */
class Fight {

    int nr
    FightPlayer fightPlayer1
    FightPlayer fightPlayer2
    int escapeAttempts = 0

    String log = ""
    String logHistory = "" // :TODO implement

    BattleType battleType
    boolean battleOver

    Date createDate = new Date()

    public String setLog(String log){
        // If where cleaning the log we take the old log and set it into the history
        if (!log){
            logHistory += this.log
        }
        this.log = log
    }


    FightPlayer myPlayer(Owner owner){
        if(owner?.id == fightPlayer1.owner?.id){
            return fightPlayer1
        }
        else if(owner?.id == fightPlayer2.owner?.id){
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
