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

            // :TODO remove test map
            player.map = Map.get(1)

            MapLayout mapLayout = createMapArray(player.map);

            render text: g.render(template: 'map', model: [mapLayout: mapLayout, player: player])
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
