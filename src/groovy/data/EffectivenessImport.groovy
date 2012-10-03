package data

import game.Effective
import game.Effectiveness

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 02-10-12
 * Time: 14:16
 * To change this template use File | Settings | File Templates.
 */
class EffectivenessImport {

    public static void importEffectiveness(){
        def file = new File('import/effectiveness.txt')

        println "Import effectiveness lines"

        int index = 0
        def parts = []
        file.eachLine { line ->
            parts.add( line )
            if (index%4==3){

                Effectiveness effectiveness = new Effectiveness(
                        attackType : parts[0],
                        type1: parts[1],
                        type2: parts[2],
                        effect : Double.parseDouble(parts[3])
                )

                effectiveness.save()

                parts = []
            }
            index++
        }
    }

}
