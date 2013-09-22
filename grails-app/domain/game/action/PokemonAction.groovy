package game.action

class PokemonAction extends CharacterAction {

    String cssClass = "actionObject spritely overworldPokemon"
    Integer correctionLeft = -8
    Integer correctionTop = -16
    int pokemonNr = 1

    public String getImage(){
        return "/images/followers/${pokemonNr}.png"
    }

}
