package game

import map.View
import map.ActionResult
import map.ActionFlow
import map.ActionTrigger

class GameController {

    FightFactoryService fightFactoryService

    def index() {
//        // Test data
//        Player player = Player.findByUsername("kevin")
//        session.playerData = new PlayerData(player.id)

    }

    def action(){

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        // Check actions
        ActionResult actionResult = ActionFlow.decideAction(player,ActionTrigger.ActionBtn,fightFactoryService)
        if (actionResult){
            render text: actionResult.evalMessage
            return
        }

        // Auto Map Transitions
        if (player.map.worldX != null && player.map.worldY != null){
            MapLayout mapLayout = MapLayout.createMapArray(player.map)
            if (player.positionY == mapLayout.rows-1){ // down
                Map toMap = Map.findByWorldXAndWorldY(player.map.worldX,player.map.worldY+1)
                if (toMap){
                    MapLayout mapToLayout = MapLayout.createMapArray(toMap)
                    String tile = mapToLayout.foreground[0][player.positionX]
                    if (tile == "" || tile == "0"){
                        player.positionY = 0
                        player.setMap toMap
                        player.save(flush:true)
                        render text: "getView();"
                    }
                }
            }
            else if (player.positionY == 0){ // up
                Map toMap = Map.findByWorldXAndWorldY(player.map.worldX,player.map.worldY-1)
                if (toMap){
                    MapLayout mapToLayout = MapLayout.createMapArray(toMap)
                    String tile = mapToLayout.foreground[mapToLayout.rows-1][player.positionX]
                    if (tile == "" || tile == "0"){
                        player.positionY = mapToLayout.rows-1
                        player.setMap toMap
                        player.save(flush:true)
                        render text: "getView();"
                    }
                }
            }
            else if (player.positionX == 0){ // left
                Map toMap = Map.findByWorldXAndWorldY(player.map.worldX-1,player.map.worldY)
                if (toMap){
                    MapLayout mapToLayout = MapLayout.createMapArray(toMap)
                    String tile = mapToLayout.foreground[player.positionY][mapToLayout.columns-1]
                    if (tile == "" || tile == "0"){
                        player.positionX = mapToLayout.columns-1
                        player.setMap toMap
                        player.save(flush:true)
                        render text: "getView();"
                    }
                }
            }
            else if (player.positionX == mapLayout.columns-1){ // right
                Map toMap = Map.findByWorldXAndWorldY(player.map.worldX+1,player.map.worldY)
                if (toMap){
                    MapLayout mapToLayout = MapLayout.createMapArray(toMap)
                    String tile = mapToLayout.foreground[player.positionY][0]
                    if (tile == "" || tile == "0"){
                        player.positionX = 0
                        player.setMap toMap
                        player.save(flush:true)
                        render text: "getView();"
                    }
                }
            }
        }

        render text : ""
    }

    def playerLocation(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        String json = """
            {"player":[{"name" : "${player.name}", "x" : "${player.positionX}", "y" : "${player.positionY}", "map" : "${player.map.name}"}]}
        """

        render text: json
    }

    def checkBattle(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view == View.Battle){
            redirect action : "view"
        }
        else {
            render text: ""
        }
    }

    def checkMove(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view == View.ShowMap){

            int direction = params.direction

            MapLayout mapLayout = MapLayout.createMapArray(player.map)

            if(direction == "3")
            {
                player.positionX = player.positionX - 1;
            }
            else if(direction == "0")
            {
                player.positionY = player.positionY - 1
            }
            else if(direction == "1")
            {
                player.positionX = player.positionX + 1
            }
            else if(direction == "2")
            {
                player.positionY = player.positionY + 1
            }

            String currentForegroundTile = getCurrentTile(mapLayout,player,false)
            if (currentForegroundTile != "0"){
                player.discard()
                render text : ""
                return
            }

            String currentBackgroundTile = getCurrentTile(mapLayout,player,true)

            if (currentBackgroundTile){

                // check pokemon
                if (checkForWildPokemon(currentBackgroundTile)){
                    MapPokemon mapPokemon = getRandomPokemon(player)
                    if (mapPokemon){
                        Fight fight = fightFactoryService.startFight(BattleType.PVE,player,mapPokemon)

                        // koppel gevecht aan speler
                        player.fightNr = fight.nr
                        player.view = View.Battle
                    }
                }

                // Check actions
                ActionResult actionResult = ActionFlow.decideAction(player,ActionTrigger.Move,fightFactoryService)
                if (actionResult){
                    if (!actionResult.allowStep){
                        // The step is disallowed
                        player.discard()
                        render text: actionResult.evalMessage + "allowMove = false;";
                    }
                    else {
                        player.save(flush: true)
                        render text: actionResult.evalMessage
                    }
                    return
                }

                render text:"allowMove = true;"
                return
            }
            else {
                player.discard()
                render text : "allowMove = false;"
                return
            }

        }

        render text: "allowMove = false;"

    }

    public boolean checkForWildPokemon(String currentTile){
        return currentTile == "01"
    }

    /**
     * Tries to find an wild pokemon.
     * @param player
     */
    public static MapPokemon getRandomPokemon(Player player)
    {
        Random random = new Random()

        int encouterPkmnNr = random.nextInt(100)+1

        if(encouterPkmnNr <= 10)
        {
            def mapPokemonList = player.map.merge().mapPokemonList

            if (mapPokemonList){
                int totalCount = mapPokemonList.sum{ it.chance }

                int random_number = random.nextInt(totalCount) + 1;

                int chanceCount = 0
                for (MapPokemon mapPokemon:mapPokemonList){

                    if (chanceCount + mapPokemon.chance >= random_number){
                        return mapPokemon
                    }
                    else {
                        chanceCount += mapPokemon.chance
                    }
                }
            }
        }
        return null
    }

    public static String getCurrentTile(MapLayout mapLayout, Player player, boolean background)
    {
        if (player.positionX >= 0 && player.positionY >= 0 && player.positionX < mapLayout.background.last().size() && player.positionY < mapLayout.background.size()){
            if(background){
                return mapLayout.background[player.positionY][player.positionX]
            }
            else {
                return mapLayout.foreground[player.positionY][player.positionX]
            }
        }

    }

    def view(){

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        if (player.view == View.ShowMap){

            MapLayout mapLayout = MapLayout.createMapArray(player.map);

            render text: g.render(template: 'map', model: [mapLayout: mapLayout, player: player])
        }
        else if (player.view == View.ShowMarket){
            redirect controller : "market", action: "index"
        }
        else if (player.view == View.ShowComputer){
            redirect controller : "party", action: "computer"
        }
        else if (player.view == View.ChoosePokemon){
            redirect controller : "choosePokemon", action: "index"
        }
        else if (player.view == View.Battle){
            render text : "<iframe src='/game/battle' frameborder='0' width='500' height='500'></iframe>"
        }
    }

}
