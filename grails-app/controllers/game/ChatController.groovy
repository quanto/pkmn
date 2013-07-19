package game

import game.context.PlayerData

class ChatController {

    ChatMessageService chatMessageService

    def index(long lastChatId) {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        String string = chatMessageService.getChatMessages(lastChatId, player)

        render text: g.render(template: '/game/breakout') + string
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

        String string = "";

        newsItems.each { NewsItem newsItem ->

            string += "<p><strong>${newsItem.player.username}:</strong><em class='chatTime'>(${newsItem.date.format("dd/MM-mm:ss")})</em> ${newsItem.message.encodeAsHTML()}</p>"
        }

        render text: g.render(template: '/game/breakout') + string
    }

}
