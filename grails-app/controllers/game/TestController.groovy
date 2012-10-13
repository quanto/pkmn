package game

import javax.imageio.ImageIO
import game.MapLayout
import game.FightFactoryService

class TestController {

    FightFactoryService fightFactoryService

    def sessionRegistry


    def test4(){
       Player player = Player.findByUsername("kevin")


        String playerData =
"""<playerData>
${player.username}
${player.name}
${player.password}
${player.enabled}
${player.mail}
${player.ip}
${player.registerDate}
${player.map.name}
${player.lastRecoverAction.map.name}
${player.lastRecoverAction.positionX}
${player.lastRecoverAction.positionY}
${player.positionX}
${player.positionY}
${player.money}
${player.view}
${player.pveBattlesWon}
${player.pveBattlesLost}
${player.pvnBattlesWon}
${player.pvnBattlesLost}
</playerData>
"""
        List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwner(player)

        String ownerPokemonData = ""

        ownerPokemonList.each { OwnerPokemon op ->
            String ownerMoves = ""
            op.ownerMoves.each { OwnerMove om ->
                ownerMoves +=
"""<ownerMove>
${om.move.name}
${om.ppLeft}
</ownerMove>
"""
            }

ownerPokemonData += """<ownerPokemon>
${op.isNpc}
${op.pokemon.nr}
${op.hpIV}
${op.attackIV}
${op.defenseIV}
${op.spAttackIV}
${op.spDefenseIV}
${op.speedIV}
${op.hp}
${op.gender}
${op.partyPosition}
${op.xp}
${op.level}
</ownerPokemon>
${ownerMoves}
"""
        }

        playerData += ownerPokemonData

        render text: playerData
    }

    def test3(){
        render text:  sessionRegistry.getAllPrincipals()
    }

    def test(){
        render text: fightFactoryService.fights
    }

    def test2(){
        // 0, 407 rows





        int total = 407 * 24;

        int i=0;

        def data = []
        def rowData = []
        for(i=0;i<total;i++)
        {
            int x = i % 8;

            int row = Math.floor(i / 24);
            int cell = Math.floor((i % 24) / 8);
            int y = row + (cell * 15);

            rowData.add("${x}${y}")

            if (i % 24 == 23)
            {
                data.add(rowData)
                rowData = []
            }
        }

        String filePath = "/images/generatedMaps/tileset.png"

        File file = new File("web-app" + filePath)

        println data

        MapLayout mapLayout = new MapLayout(
            background: data
        )

        ImageIO.write(mapLayout.writeTiles(data), "png", file)
        render text: "done"
    }

    def index() {                  //
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
