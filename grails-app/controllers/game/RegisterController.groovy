package game

import org.apache.commons.lang.RandomStringUtils

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

    def forgotPassword(){
        if (request.post && params.email?.trim()){
            Player player = Player.findByEmail(params.email.trim())
            if (player && player.email){
                player.recoverPasswordSecret = RandomStringUtils.random(15, true, true)
                sendMail {
                    to player.email
                    subject "Recover your password"
                    body g.render(template: 'recoverPasswordMail', model: [player: player])
                }
                player.save()
                flash.message = "Check your mail"
            }
            else {
                flash.message = "E-mailadres not found"
            }
        }
        render view : "forgotPassword"
    }

    def recoverPassword(){
        if (request.post){
            Player player = Player.findByRecoverPasswordSecret(params.secret)
            if (player){
                player.password = params.password
                player.recoverPasswordSecret = null
                player.save()
                flash.message = "Your password has been changed"
                redirect action: 'index'
                return
            }
        }
        render view:"recoverPassword", model : [secret:params.secret]
    }

}
