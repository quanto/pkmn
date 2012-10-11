package data

import game.Effective
import game.Effectiveness

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
