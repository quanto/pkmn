package game

import map.View

class GameController {

    FightFactoryService fightFactoryService

    def index() {
        // Test data
        Player player = Player.findByUsername("kevin")
        session.playerData = new PlayerData(player.id)

    }

    def action(){

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()


        Action action = Action.findByMapAndPositionXAndPositionY(player.map,player.positionX,player.positionY)

        if (action){

            if (action in MapTransition){
                MapTransition mapTransition = (MapTransition)action
                player.positionX = mapTransition.jumpTo.positionX
                player.positionY = mapTransition.jumpTo.positionY
                player.setMap mapTransition.jumpTo.map
                player.save(flush:true)
                render text: "refreshMap"
            }
            else if (action in MapMessage){
                MapMessage mapMessage = (MapMessage)action
                render text: mapMessage.message
            }
            else if (action in RecoverAction){
                Recover.recoverParty(player)
                render text: "Your pokemon have been recovered!"
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

            String currentTile = getCurrentTile(mapLayout,player);

            if (currentTile){

                if (checkForWildPokemon(currentTile)){
                    MapPokemon mapPokemon = getRandomPokemon(player)
                    if (mapPokemon){
                        Fight fight = fightFactoryService.startFight(BattleType.PVE,player,mapPokemon)

                        // koppel gevecht aan speler
                        player.fightNr = fight.nr
                        player.view = View.Battle
                    }
                }

                player.save(flush: true)
                render text : "1"
            }
            else {
                player.discard()
                render text : "0"
            }

        }
        else {
            render text: ""
        }
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

        int encouterPkmnNr = random.nextInt(100)

        if(encouterPkmnNr < 30)
        {
            def mapPokemonList = player.map.merge().mapPokemonList

            if (mapPokemonList){
                int totalCount = mapPokemonList.sum{ it.chance }

                int random_number = random.nextInt(totalCount);

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

    public static String getCurrentTile(MapLayout mapLayout, Player player)
    {
        if (player.positionX >= 0 && player.positionY >= 0 && player.positionX < mapLayout.background.last().size() && player.positionY < mapLayout.background.size()){
            return mapLayout.background[player.positionY][player.positionX]
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

        }
        else if (player.view == View.ShowComputer){

        }
        else if (player.view == View.ChosePokemon){
            render text: g.render(template: 'chosePokemon')
        }
        else if (player.view == View.Battle){
            render text : "<iframe src='/game/battle' frameborder='0' width='500' height='300'></iframe>"
        }
    }

}
