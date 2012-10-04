package game

import map.View

class MapController {

    def index() {
        // Test data
        Owner owner = Owner.findByName("Kevin")
        session.owner = owner


    }

    def party(){
        Player player = session.owner

        boolean computerView = false
        def ownerPokemonList = OwnerPokemon.findAllByPartyPositionGreaterThanAndOwner(0,player)

        render text: g.render(template: 'party',model: [computerView:computerView,ownerPokemonList:ownerPokemonList])
    }


    def view(){

        Player player = session.owner

        if (player.view == View.ShowMap){

            Map map = Map.get(1)
            MapLayout mapLayout = createMapArray(map);

            render text: g.render(template: 'map', model: [mapLayout:mapLayout])
        }
        else if (player.view == View.ShowMarket){

        }
        else if (player.view == View.ShowComputer){

        }
        else if (player.view == View.ChosePokemon){
            render text: g.render(template: 'chosePokemon')
        }
        else if (player.view == View.Battle){

        }
    }

    public static void createTheMap()
    {

        //Tile formaat
//        tilesSize["x"] = 16;
//        tilesSize["y"] = 16;

        //Get player info
//        playerData = player::getPlayerData();
//
//        //Get Other players
//        otherPlayers = player::getOtherPlayers();
//
//        playerLocX = playerData['positionX'];
//        playerLocY = playerData['positionY'];
//        loadMap = playerData['map'];

        //return loadMap."jaa";


        //Create the actual map


    }

    public static MapLayout createMapArray(Map map)    // mapName = ""
    {
//        if(mapName == "")
//        {
//            playerData = player::getPlayerData();
//            mapName = playerData["map"];
//        }

        def mapRowsBackground = map.databackground.split('-')
        def mapRowsForeground = map.dataForeground.split('-')



        MapLayout mapLayout = new MapLayout()
//        tilesInfo = new tiles;
//        tiles = (tilesInfo -> getBackgroundTiles());

        mapLayout.background = new int[mapRowsBackground[0].split(',').length][mapRowsBackground.size()]
        mapLayout.background = []
        for(int y=0; y<mapRowsBackground.length; y++)
        {
            def tileNr = mapRowsBackground[y].split(',')

            def row = []
            for(int x=0; x < tileNr.length; x++)
            {
                row.add(tileNr[x])
            }
            println row
            mapLayout.background.add(row)
        }

//        for(y=0; y<sizeof(mapRowsForeground); y++)
//        {
//            tileNr = explode(",",mapRowsForeground[y]);
//
//            for(x=0; x<sizeof(tileNr); x++)
//            {
//                mapArray["foreground"][x][y] = tileNr[x];
//            }
//        }

        return mapLayout;
    }

//    function getMapArray()
//    {
//        return this->mapArray;
//    }
//
//    static function getMap(playerId)
//    {
//        sql =  mysql_query("SELECT dataForeground, dataBackground FROM maps WHERE name = '".playerId."'") or die(mysql_error());
//        result2 = mysql_fetch_array (sql);
//
//        return result2;
//    }
//
//    static function getMapData(mapName)
//    {
//        sql =  mysql_query("SELECT dataForeground, dataBackground FROM maps WHERE name = '".mapName."'") or die(mysql_error());
//        result2 = mysql_fetch_array (sql);
//
//        return result2;
//    }
//
//    static function getMapSize(mapArray)
//    {
//        //print_r(mapArray["foreground"]);
//        dim["x"] = sizeof(mapArray["foreground"]);
//        dim["y"] = sizeof(mapArray["foreground"][1]);
//
//        return dim;
//    }

}
