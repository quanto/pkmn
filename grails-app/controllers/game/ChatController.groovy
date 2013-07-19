package game

import game.context.PlayerData
import game.social.ChatScope

class ChatController {

    def index(long lastChatId) {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        List<ChatMessage> chatMessageList = ChatMessage.executeQuery("from ChatMessage c where (((c.map = ? OR c.map IS NULL) AND c.chatScope != ?) OR (c.chatScope = ? AND (c.player = ? OR c.toPlayer = ?))) AND c.id > ?",[player.map, ChatScope.Private, ChatScope.Private, player, player, lastChatId], [max:50,sort: 'id', order:'asc'])

        String string = chatMessageList?"<script>lastChatId = '${chatMessageList.last().id}';</script>":'';

        chatMessageList.each { ChatMessage chatMessage ->

            string += "<div class='${chatMessage.chatScope.name()}'><strong>${chatMessage.player.username}@${chatMessage.chatScope.name()}:</strong><em class='chatTime'>(${chatMessage.date.format("dd/MM-mm:ss")})</em> ${chatMessage.message.encodeAsHTML()}</div>"
        }

        render text: g.render(template: '/game/breakout') + string
    }

    def send(String chatScope, String message){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        ChatMessage chatMessage = new ChatMessage(
                chatScope: ChatScope.valueOf(chatScope),
                message: message,
                player: player
        )

        if (message.startsWith("@")){
            List messageParts = message.split(' ')
            String username = messageParts[0].replace('@','')
            Player toPlayer = Player.findByUsername(username)
            if (toPlayer){
                messageParts.remove(0)
                chatMessage.message = messageParts.join(' ')
                chatMessage.toPlayer = toPlayer
                chatMessage.chatScope = ChatScope.Private
            }
        }

        if (chatMessage.chatScope == ChatScope.Map){
            chatMessage.map = player.map
        }

        chatMessage.save()

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
