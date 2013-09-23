package map

import game.Player

class MessageTranslator {

    public static String proces(String message, Player player){
        return message?.replaceAll('#name',player.name)
    }

}
