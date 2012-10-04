package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 29-09-12
 * Time: 08:08
 * To change this template use File | Settings | File Templates.
 */
class Tile {
    String mainCat
    String subCat1
    String subCat2
    String subCat3
    String url
    boolean walkable
    boolean tileBlock
    Integer xDim
    Integer yDim

    static constraints = {
        xDim nullable :true
        yDim nullable :true
    }

}
