package data

import game.Tile

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 04-10-12
 * Time: 14:34
 * To change this template use File | Settings | File Templates.
 */
class TilesImport {

    public static void importTiles(){
        def file = new File('import/tiles.txt')

        println "Import tile lines"

        int index = 0
        def parts = []
        file.eachLine { line ->
            parts.add( line )
            if (index%10==9){
                Tile tile = new Tile(
                    id: Integer.parseInt(parts[0]),
                    mainCat : parts[1],
                    subCat1 : parts[2],
                    subCat2 : parts[3],
                    subCat3 : parts[4],
                    url : parts[5],
                    walkable : parts[6] == '1',
                    tileBlock : parts[7] == '1',
                    xDim : parts[8]?Integer.parseInt(parts[8]):null,
                    yDim : parts[9]?Integer.parseInt(parts[9]):null
                )

                tile.save()

                parts = []
            }
            index++
        }
    }

}
