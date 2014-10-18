package game

import game.social.ChatScope
import grails.plugin.springsecurity.SpringSecurityUtils

class ChatMessageService {

    private static List<ChatMessage> chatMessages = []
    private static long chatCount = 0

    static transactional = false

    String getChatMessages(Long lastChatId, Player player){

        // Remove old messages
        chatMessages.removeAll{ it.date < new Date()-1 }

        def chatMessageList = chatMessages.findAll{ ChatMessage chatMessage ->

            if (chatMessage.id <= lastChatId){
                return false
            }
            else if (chatMessage.chatScope == ChatScope.Map && chatMessage.map?.id == player.map?.id){
                return true
            }
            else if (chatMessage.chatScope == ChatScope.Global){
                return true
            }
            else if (chatMessage.chatScope == ChatScope.Friends && (player.friends.find{ it.id == chatMessage.player.id || player.id == chatMessage.player.id })){
                return true
            }
            else if (chatMessage.chatScope == ChatScope.Private && (player.id == chatMessage.player.id || player.id == chatMessage.toPlayer.id)){
                return true
            }

            return false
        }.sort{ !it.date }.collate(100)[0].sort{ it.date }

        String string = chatMessageList?"<script>lastChatId = '${chatMessageList.max{it.id}.id }';</script>":'';

        chatMessageList.each { ChatMessage chatMessage ->

            string += "<div class='${chatMessage.chatScope.name()}'><strong>${chatMessage.player.username}@${chatMessage.chatScope==ChatScope.Private?chatMessage.toPlayer.username:chatMessage.chatScope.name()}:</strong><em class='chatTime'>(${chatMessage.date.format("dd/MM-mm:ss")})</em> ${chatMessage.message.encodeAsHTML()}</div>"
        }

        return string
    }

    void send(String chatScope, String message, Player player) {
        ChatScope scope = ChatScope.valueOf(chatScope)
        if ((!SpringSecurityUtils.ifAllGranted('ROLE_ADMIN') && scope == ChatScope.Global) || scope == ChatScope.Private){
            return
        }

        chatCount += 1

        ChatMessage chatMessage = new ChatMessage(
                id: chatCount,
                chatScope: scope,
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

        chatMessages.add(0,chatMessage);
    }
}
