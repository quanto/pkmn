package game

import game.context.PlayerData

class ChatController {

    def index() {
        List<ChatMessage> chatMessageList = ChatMessage.list(max:50,sort: 'id', order:'desc').reverse()

        String string = "";

        chatMessageList.each { ChatMessage chatMessage ->

            string += "<strong>${chatMessage.player.name}:</strong><em class='chatTime'>(${chatMessage.date.format("dd/MM-mm:ss")})</em> ${chatMessage.message.encodeAsHTML()}<br />"
        }

        render text: g.render(template: '/game/breakout') + string
    }

    def send(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        new ChatMessage(
                message: params.chatMessage,
                player: player
        ).save()
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
