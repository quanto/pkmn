package game

import map.View

class MapController {

    def index() {
        // Test data
        Owner owner = Owner.findByName("Kevin")
        session.owner = owner


    }

    def party(){
        Player player = session.owner

        boolean computerView = false
        def ownerPokemonList = OwnerPokemon.findAllByPartyPositionGreaterThanAndOwner(0,player)

        render text: g.render(template: 'party',model: [computerView:computerView,ownerPokemonList:ownerPokemonList])
    }


    def view(){

        Player player = session.owner

        if (player.view == View.ShowMap){

        }
        else if (player.view == View.ShowMarket){

        }
        else if (player.view == View.ShowComputer){

        }
        else if (player.view == View.ChosePokemon){
            render text: g.render(template: 'chosePokemon')
        }
        else if (player.view == View.Battle){

        }
    }
}
