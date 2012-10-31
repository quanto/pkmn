package data

import game.Move

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 01-10-12
 * Time: 19:14
 * To change this template use File | Settings | File Templates.
 */
class MoveImport {
    
    public static void importMoves(){
        def file = new File('import/moves.txt')

        println "Import move lines"

        int index = 0
        def parts = []
        file.eachLine { line ->
            parts.add( line )
            if (index%11==10){

                Move move = new Move(
                        id: Integer.parseInt(parts[0]),
                        name: parts[1],
                        type: parts[2],
                        category : parts[3],
                        power : Integer.parseInt(parts[4]),
                        accuracy : Integer.parseInt(parts[5]),
                        pp : Integer.parseInt(parts[6]),
                        effect : parts[7],
                        effectProb : Integer.parseInt(parts[8]),
                        implemented : Integer.parseInt(parts[9]),
                        priority: Integer.parseInt(parts[10])
                )
                move.save()

                parts = []
            }
            index++
        }
    }
    
}
