package game

import map.View
import game.context.PlayerData

class ChoosePokemonController {

    def index() {

    }

    def choose(){

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view == View.ChoosePokemon){

            int pkmnId = Integer.parseInt(params.id)

            if(pkmnId == 1 || pkmnId == 4 || pkmnId == 7)
            {
                PokemonCreator.addOwnerPokemonToOwner(Pokemon.get(pkmnId), 5, player)
                player.view = View.ShowMap
                player.save(fllush: true)
                render text: ""
            }
        }
    }

}
