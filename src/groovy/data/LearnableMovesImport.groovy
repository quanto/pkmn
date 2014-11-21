package data

import game.Pokemon
import game.LearnableMove
import game.Move

class LearnableMovesImport {

    public static void importLearnableMoves(){
        println "Import learnableMove lines"

        def file = Thread.currentThread().getContextClassLoader().getResourceAsStream('import/learnableMoves.txt')

        int index = 0
        def parts = []
        file.eachLine { line ->
            parts.add( line )
            if (index%3==2){
                if (Integer.parseInt(parts[0]) <= 150){
                    Move move = Move.findByNr(Integer.parseInt(parts[2]))
                    if (move){
                        LearnableMove learnableMove = new LearnableMove(
                                pokemon : Pokemon.get(Integer.parseInt(parts[0])),
                                learnLevel : Integer.parseInt(parts[1]),
                                move : move
                        )

                        learnableMove.save()
                    }
                    else {
                        println "Skipping learnable move moveNr: ${parts[2]}"
                    }
                    parts = []
                }
            }
            index++
        }
    }


}
