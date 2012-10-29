package game.context

import game.Player

class PlayerData {

    long playerId

    public PlayerData(long id){
        playerId = id
    }

    public Player getPlayer(){
        return Player.get(playerId)
    }

}
