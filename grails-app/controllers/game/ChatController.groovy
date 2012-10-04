package game

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
        Player player = session.owner

        new ChatMessage(
                message: params.chatMessage,
                player: player
        ).save()
        render text: ""
    }

}
