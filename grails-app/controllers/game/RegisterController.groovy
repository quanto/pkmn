package game

class RegisterController {

    def index() {

        Player player

        if (request.post){
            player = new Player(params)
            player.lastRecoverAction = RecoverAction.findByMap(Map.findByName("Glooming forest center"))
            player.map = Map.findByName("Glooming forest -2x0")
            player.positionX = 12
            player.positionY = 12
            if (player.validate()){
                player.save()
                redirect controller: 'game'
            }
        }

        render view : 'register', model : [player:player ]
    }
}
