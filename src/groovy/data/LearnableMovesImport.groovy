package data

import game.Pokemon
import game.LearnableMove
import game.Move

class LearnableMovesImport {

    public static void importLearnableMoves(){
        println "Import learnableMove lines"
        def file = new File('import/learnableMoves.txt')

        int index = 0
        def parts = []
        file.eachLine { line ->
            parts.add( line )
            if (index%3==2){
                if (Integer.parseInt(parts[0]) <= 150){

                    LearnableMove learnableMove = new LearnableMove(
                            pokemon : Pokemon.get(Integer.parseInt(parts[0])),
                            learnLevel : Integer.parseInt(parts[1]),
                            move : Move.get(Integer.parseInt(parts[2]))
                    )

                    learnableMove.save()

                    parts = []
                }
            }
            index++
        }
    }


}
