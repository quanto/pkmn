package game

import game.context.PlayerData

class ChatController {

    def index() {
        List<ChatMessage> chatMessageList = ChatMessage.list(max:50)

        String string = "";

        chatMessageList.each { ChatMessage chatMessage ->

            string += "<strong>${chatMessage.player.name}:</strong><em class='chatTime'>(${chatMessage.date.format("dd/MM-mm:ss")})</em> ${chatMessage.message.encodeAsHTML()}<br />"
        }

        render text: string
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

}
