package game.context

import game.fight.RoundResult
import game.context.FightPlayer
import game.context.BattleType
import game.OwnerPokemon
import game.Owner

class Fight {

    int nr
    FightPlayer fightPlayer1
    FightPlayer fightPlayer2
    int escapeAttempts = 0
    List<FightPokemon> usedFightPokemon = [] // The used pokemon in a round to who we should give Exp to

    String log = ""
    RoundResult roundResult = new RoundResult()

    String logHistory = ""

    BattleType battleType
    boolean battleOver
    boolean player1first
    // Round where one or both users must switch
    boolean switchRound = false

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

    public FightPlayer getFirstFightPlayer(){
        player1first?fightPlayer1:fightPlayer2
    }

    public FightPlayer getSecondFightPlayer(){
        !player1first?fightPlayer1:fightPlayer2
    }

}
