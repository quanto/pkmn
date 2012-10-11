package game

class RegisterController {

    def index() {

        Player player

        if (request.post){
            player = new Player(params)
            player.lastRecoverAction = RecoverAction.list().last()
            if (player.validate()){
                player.save()
                redirect controller: 'game'
            }
        }

        render view : 'register', model : [player:player ]
    }
}
