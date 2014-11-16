package game

import game.context.PlayerData
import grails.converters.JSON

class ChatController {

    ChatMessageService chatMessageService

    def index(long lastChatId) {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        List<ChatMessage> chatMessageList = chatMessageService.getChatMessages(lastChatId, player)

        render chatMessageList.collect { ChatMessage chatMessage ->
            [
                    id: chatMessage.id,
                    scope: chatMessage.chatScope.name(),
                    username: chatMessage?.player?.username,
                    date: chatMessage.date.format("dd/MM-mm:ss"),
                    message: chatMessage.message,
                    toUsername: chatMessage?.toPlayer?.username
            ]
        } as JSON
    }

    def send(String chatScope, String message){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        chatMessageService.send(chatScope, message, player)

        render text: ""
    }

    def showNewsItems()
    {
        List<NewsItem> newsItems = NewsItem.list(max:20)

        render newsItems.collect{[
                username: it.player.username,
                date: it.date.format("dd/MM"),
                message: it.message
        ]}  as JSON
    }

}
