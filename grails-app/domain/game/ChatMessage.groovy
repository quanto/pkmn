package game

import game.social.ChatScope

class ChatMessage {

    Date date = new Date()
    String message
    Player player
    ChatScope chatScope
    Map map
    Player toPlayer

    static constraints = {
        map nullable: true
        toPlayer nullable: true
    }

    static mapping = {
        order "desc"
    }

}
