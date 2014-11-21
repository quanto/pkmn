package data

import game.Pokemon

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 01-10-12
 * Time: 19:17
 * To change this template use File | Settings | File Templates.
 */
class PokemonImport {

    public static void importPokemon(){



        def file = Thread.currentThread().getContextClassLoader().getResourceAsStream('import/pokemon.txt')
        println "Import pokemon lines"

        int index = 0
        def parts = []
        file.eachLine { line ->
            parts.add( line )
            if (index%17==16){

                Pokemon pokemon = new Pokemon(
                        nr : Integer.parseInt(parts[0]),
                        name : parts[1],
                        type1 : parts[2],
                        type2 : parts[3],
                        baseHp : Integer.parseInt(parts[4]),
                        baseAttack : Integer.parseInt(parts[5]),
                        baseDefense : Integer.parseInt(parts[6]),
                        baseSpAttack : Integer.parseInt(parts[7]),
                        baseSpDefense : Integer.parseInt(parts[8]),
                        baseSpeed : Integer.parseInt(parts[9]),
                        maleRate : Double.parseDouble(parts[10]),
                        femaleRate : Double.parseDouble(parts[11]),
                        catchRate : Integer.parseInt(parts[12]),
                        baseEXP : Integer.parseInt(parts[13]),
                        levelRate : parts[14],
                        height : parts[15],
                        weight : parts[16]
                )

                pokemon.save()

                parts = []
            }
            index++
        }
    }

}
