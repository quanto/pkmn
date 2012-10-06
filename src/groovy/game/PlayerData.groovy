package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 06-10-12
 * Time: 07:31
 * To change this template use File | Settings | File Templates.
 */
class PlayerData {

    long playerId

    public PlayerData(long id){
        playerId = id
    }

    public Player getPlayer(){
        return Player.get(playerId)
    }

}
