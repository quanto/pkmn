package game

class AdminController {

    def index() {
        List<ChatMessage> chatMessageList = ChatMessage.list(max:50)

        String string = "";

        chatMessageList.each { ChatMessage chatMessage ->

            string += "<strong>${chatMessage.player.name}:</strong><em class='chatTime'>(${chatMessage.date.format("dd/MM-mm:ss")})</em> ${chatMessage.message.encodeAsHTML()}<br />"
        }

        render view: "index"
    }
	
	def news() {
		render template: "news"
	}
	
	def saveNewsItem() {
		PlayerData playerData = session.playerData
		Player player = playerData.getPlayer()

		println params
		
		new NewsItem(
				message: params.newsItem,
				player: player
		).save()
		redirect action: "index"
	}
	
	def showNewsItems()
	{
		List<NewsItem> newsItems = NewsItem.list(max:20)
		
		String string = "";

		newsItems.each { NewsItem newsItem ->

			string += "<strong>${newsItem.player.name}:</strong><em class='chatTime'>(${newsItem.date.format("dd/MM-mm:ss")})</em> ${newsItem.message.encodeAsHTML()}<br />"
		}

		render text: string
	}

}
