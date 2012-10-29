package game

import map.View
import game.context.PlayerData

class ChoosePokemonController {

    def index() {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        render text: g.render(template: 'choosePokemon')
    }

    def choose(){

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view == View.ChoosePokemon){

            int pkmnId = Integer.parseInt(params.pkmn)

            if(pkmnId == 1 || pkmnId == 4 || pkmnId == 7)
            {
                PokemonCreator.addOwnerPokemonToOwner(Pokemon.get(pkmnId), 5, player)
                player.view = View.ShowMap
                player.save()
                render text: ""
            }
        }
    }

}
