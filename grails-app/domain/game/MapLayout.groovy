package game

/**
 * Created with IntelliJ IDEA.
 * User: kevinverhoef
 * Date: 04-10-12
 * Time: 15:12
 * To change this template use File | Settings | File Templates.
 */
class MapLayout {

    def background = []
    def foreground = []

    public int getRows(){
        return background.size()
    }

    public int getColumns(){
        if (background.size()){
            return background.last().size()
        }
    }

    public static MapLayout createMapArray(Map map)    // mapName = ""
    {
//        if(mapName == "")
//        {
//            playerData = player::getPlayerData();
//            mapName = playerData["map"];
//        }

        MapLayout mapLayout = new MapLayout()

//        tilesInfo = new tiles;
//        tiles = (tilesInfo -> getBackgroundTiles());

        def mapRowsBackground = map.databackground.split('-')

        mapLayout.background = []
        for(int y=0; y<mapRowsBackground.length; y++)
        {
            def tileNr = mapRowsBackground[y].split(',')

            def row = []
            for(int x=0; x < tileNr.length; x++)
            {
                row.add(tileNr[x])
            }
            mapLayout.background.add(row)
        }

        def mapRowsForeground = map.dataForeground.split('-')
        mapLayout.foreground = []
        for(int y=0; y<mapRowsForeground.length; y++)
        {
            def tileNr = mapRowsForeground[y].split(',')

            def row = []
            for(int x=0; x < tileNr.length; x++)
            {
                row.add(tileNr[x])
            }
            mapLayout.foreground.add(row)
        }

        return mapLayout;
    }

}
