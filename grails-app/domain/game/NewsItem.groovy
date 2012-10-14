package game

class NewsItem {

    Date date = new Date()
    String message
    Player player

    static mapping = {
        message type:"text"
    }

    static constraints = {
    }

}
