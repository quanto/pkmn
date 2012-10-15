package game.admin

import game.Map
import game.MapPokemon

class PokemonEditorController {

    def edit() {
        Map map = Map.get(params.id)
        render view:"edit", model: [map:map,mapPokemonList:map.mapPokemonList]
    }

    def remove(){
        Map map = Map.get(params.id)
        MapPokemon mapPokemon = MapPokemon.get(params.mapPokemon)
        map.removeFromMapPokemonList(mapPokemon)
        mapPokemon.delete()
        redirect action: 'edit', id: params.id
    }

    def add(){
        Map map = Map.get(params.id)

        MapPokemon mapPokemon = new MapPokemon(params)
        map.addToMapPokemonList(mapPokemon)
        redirect action: 'edit', id: params.id
    }
}
