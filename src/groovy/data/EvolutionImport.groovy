package data

import game.Evolution
import game.Pokemon

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 01-10-12
 * Time: 20:02
 * To change this template use File | Settings | File Templates.
 */
class EvolutionImport {

    public static void importEvolution(){

        def file = Thread.currentThread().getContextClassLoader().getResourceAsStream('import/evolution.txt')

        println "Import evolution lines"

        int index = 0
        def parts = []
        file.eachLine { line ->
            parts.add( line )
            if (index%4==3){

                Evolution evolution = new Evolution(
                        fromPokemon : Pokemon.get(Integer.parseInt(parts[0])),
                        toPokemon: Pokemon.get(Integer.parseInt(parts[2])),
                        level: Integer.parseInt(parts[1]),
                        condition : parts[3]
                )

                evolution.save()

                parts = []
            }
            index++
        }
    }

}
