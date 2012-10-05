package data

import game.Map

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 04-10-12
 * Time: 09:37
 * To change this template use File | Settings | File Templates.
 */
class MapImport {

    public static void importMaps(){
        println "Import map lines"
        def file = new File('import/maps.txt')

        int index = 0
        def parts = []
        file.eachLine { line ->
            parts.add( line )
            if (index%5==4){

                Map map = new Map(
                        name : parts[0],
                        dataForeground : parts[1],
                        dataBackground : parts[2],
                        pokemon: parts[3],
                        active : parts[0] == '1'
                )

                map.save()

                parts = []

            }
            index++
        }
    }

}
