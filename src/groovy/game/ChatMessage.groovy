package game

import game.social.ChatScope

class ChatMessage {
    long id
    Date date = new Date()
    String message
    Player player
    ChatScope chatScope
    Map map
    Player toPlayer
}
