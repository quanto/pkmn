package game

class TestController {

    FightFactoryService fightFactoryService

    def test(){
        render text: fightFactoryService.fights
    }

    def index() {
        def test = """
            if (pokemonType1 == "fire" && pokemonType2 == "")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "")
                effectiveness = 0.5
            else if (pokemonType1 == "electric" && pokemonType2 == "")
                effectiveness = 0.5
            else if (pokemonType1 == "ice" && pokemonType2 == "")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "")
                effectiveness = 2
            else if (pokemonType1 == "steel" && pokemonType2 == "")
                effectiveness = 0.5
            else if (pokemonType1 == "normal" && pokemonType2 == "water")
                effectiveness = 0.5
            else if (pokemonType1 == "fire" && pokemonType2 == "fighting")
                effectiveness = 0.5
            else if (pokemonType1 == "fire" && pokemonType2 == "ground")
                effectiveness = 0.5
            else if (pokemonType1 == "fire" && pokemonType2 == "flying")
                effectiveness = 0.5
            else if (pokemonType1 == "fire" && pokemonType2 == "steel")
                effectiveness = 0.25
            else if (pokemonType1 == "water" && pokemonType2 == "electric")
                effectiveness = 0.25
            else if (pokemonType1 == "water" && pokemonType2 == "grass")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "fighting")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "poison")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "ground")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "flying")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "psychic")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "dragon")
                effectiveness = 0.5
            else if (pokemonType1 == "water" && pokemonType2 == "steel")
                effectiveness = 0.25
            else if (pokemonType1 == "water" && pokemonType2 == "dark")
                effectiveness = 0.5
            else if (pokemonType1 == "electric" && pokemonType2 == "flying")
                effectiveness = 0.5
            else if (pokemonType1 == "electric" && pokemonType2 == "ghost")
                effectiveness = 0.5
            else if (pokemonType1 == "electric" && pokemonType2 == "steel")
                effectiveness = 0.25
            else if (pokemonType1 == "ice" && pokemonType2 == "grass")
                effectiveness = 2
            else if (pokemonType1 == "ice" && pokemonType2 == "ground")
                effectiveness = 2
            else if (pokemonType1 == "ice" && pokemonType2 == "flying")
                effectiveness = 2
            else if (pokemonType1 == "ice" && pokemonType2 == "psychic")
                effectiveness = 2
            else if (pokemonType1 == "ice" && pokemonType2 == "ghost")
                effectiveness = 2
            else if (pokemonType1 == "fighting" && pokemonType2 == "steel")
                effectiveness = 0.5
            else if (pokemonType1 == "ground" && pokemonType2 == "rock")
                effectiveness = 2
            else if (pokemonType1 == "bug" && pokemonType2 == "water")
                effectiveness = 0.5
            else if (pokemonType1 == "bug" && pokemonType2 == "rock")
                effectiveness = 2
            else if (pokemonType1 == "bug" && pokemonType2 == "steel")
                effectiveness = 0.5
            else if (pokemonType1 == "rock" && pokemonType2 == "grass")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "ground")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "flying")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "psychic")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "bug")
                effectiveness = 2
            else if (pokemonType1 == "rock" && pokemonType2 == "dark")
                effectiveness = 2
            else if (pokemonType1 == "dark" && pokemonType2 == "fire")
                effectiveness = 0.5
            else if (pokemonType1 == "dark" && pokemonType2 == "ice")
                effectiveness = 2
            else if (pokemonType1 == "steel" && pokemonType2 == "ground")
                effectiveness = 0.5
            else if (pokemonType1 == "steel" && pokemonType2 == "flying")
                effectiveness = 0.5
            else if (pokemonType1 == "steel" && pokemonType2 == "psychic")
                effectiveness = 0.5
            else if (pokemonType1 == "steel" && pokemonType2 == "dragon")
                effectiveness = 0.5
        """
        test.split('pokemonType1 == "').each {
            def parts = it.split('"')
                if (parts.size() > 2){
                    def effect =  parts[3].split("effectiveness = ").last().split('\\n')[0]
                    if (effect != "1"){
                        render text:  "steel"
                        render text:  "<br />"
                        render text:  parts[0]
                        render text:  "<br />"
                        render text:  parts[2]
                        render text:  "<br />"

                        render text: effect
                        render text:  "<br />"
                    }
                }
//            it.split('" && pokemonType2 == "').each {
//                println it.split('"')
//            }
        }
    }
}
