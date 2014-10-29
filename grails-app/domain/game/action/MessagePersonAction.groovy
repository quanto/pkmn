package game.action

class MessagePersonAction extends PersonAction {

    String message
    String actionFunction = "messagePerson"

    static constraints = {
        message nullable: true
        macro nullable: true
    }

}
