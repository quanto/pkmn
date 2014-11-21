package data

import game.Move
import game.context.MoveCategory

class MoveImport {
    
    public static void importMoves(){

        def file = Thread.currentThread().getContextClassLoader().getResourceAsStream('import/moves.txt')

        println "Import move lines"

        int index = 0
        def parts = []
        file.eachLine { line ->
            parts.add( line )
            if (index%11==10){

                Move move = new Move(
                        nr: Integer.parseInt(parts[0]),
                        name: parts[1],
                        type: parts[2],
                        power : Integer.parseInt(parts[4]),
                        accuracy : Integer.parseInt(parts[5]),
                        pp : Integer.parseInt(parts[6]),
                        effect : parts[7],
                        effectProb : Integer.parseInt(parts[8]),
                        implemented : Integer.parseInt(parts[9]),
                        priority: Integer.parseInt(parts[10])
                )
                if (parts[3] == "special move"){
                    move.category = MoveCategory.SpecialMove
                }
                else if (parts[3] == "physical move"){
                    move.category = MoveCategory.PhysicalMove
                }
                else if (parts[3] == "status move"){
                    move.category = MoveCategory.StatusMove
                }
                else {
                    assert false
                }

                move.save()

                parts = []
            }
            index++
        }
    }
    
}
