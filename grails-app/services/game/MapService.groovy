package game

import game.action.CharacterAction
import game.action.MessagePersonAction
import game.action.MessagePokemonAction
import game.context.ActionType
import game.context.BattleType
import game.context.Fight
import game.lock.OneTimeActionLock
import map.ActionFlow
import map.ActionResult
import map.ActionTrigger
import map.MapAction
import map.MessageTranslator
import map.View

class MapService {

    FightFactoryService fightFactoryService
    static transactional = false

    public def getMapModel(Player player, def direction){
        MapLayout mapLayout = MapLayout.createMapArray(player.map, player.altMap);

        // MapLayout mapLayout
        def blockObjects = []
        def pokemonObjects = []
        mapLayout.foreground.eachWithIndex { def row, def y ->
            row.eachWithIndex { def tileNr, def x ->
                if (tileNr && tileNr != "0"){
                    blockObjects.add([y,x])
                }
                else if (tileNr == "0" && mapLayout.background[y][x] == "01"){
                    pokemonObjects.add([y,x])
                }
            }
        }

        def actions
        if (player.altMap){
            actions = player.altMap.actions.collect{ it }
        }
        else {
            actions = player.map.actions.collect{ it }
        }
        // remove one time actions
        actions.removeAll { it.placeOneTimeActionLock && OneTimeActionLock.findByPlayerAndAction(player, it) }

        def mapModel = [
                map : [
                        name:  player.map.name,
                        id: player.map.id,
                        rows: mapLayout.getRows(),
                        columns: mapLayout.getColumns(),
                        foregroundImage: player.map.getForegroundImage(player.altMap),
                        backgroundImage: player.map.getBackgroundImage(player.altMap),

                ],
                player: [
                        direction: direction,
                        y: player.positionY,
                        x: player.positionX,
                        characterImage: player.characterImage.toString(),
                ],
                pokemonObjects: pokemonObjects.collect {[
                        y: it[0],
                        x: it[1]
                ]},
                blockObjects: blockObjects.collect {[
                        y: it[0],
                        x: it[1]
                ]},
                actionObjects: getActionObjects(actions, player)
        ]
        return mapModel
    }

    private def getActionObjects(def actions, Player player){
        return actions.collect{
            def action = [
                    id: "actionObject" + it.id,
                    clientAction: it.actionType == ActionType.Client || it.actionType == ActionType.Mixed,
                    serverAction: it.actionType == ActionType.Server || it.actionType == ActionType.Mixed,
                    y: it.positionY,
                    x: it.positionX,
                    cssClass: it.cssClass?:'',
                    triggerBeforeStep: it.triggerBeforeStep,
                    triggerOnActionButton: it.triggerOnActionButton,
                    action: it.actionFunction,
                    correctionLeft: it.correctionLeft?:0,
                    correctionTop: it.correctionTop?:0,
            ]

            if (it.image){
                action.backgroundImage = it.image
            }
            if (it in CharacterAction){
                if (it.macro){
                    action.macro = it.macro
                }
                if (it.initialDirection){
                    action.initialDirection = it.initialDirection
                }
            }
            if (it in MessagePersonAction || it in MessagePokemonAction){
                action.message = MessageTranslator.proces(it.message, player)
            }

            return action
        }
    }

    public ActionResult getActionModel(Player player, String direction){
        MapLayout mapLayout = MapLayout.createMapArray(player.map, player.altMap)

        ActionResult actionResult = new ActionResult()

        // Check position
        String currentForegroundTile = getCurrentTile(mapLayout,player,false)
        if (!currentForegroundTile || currentForegroundTile != "0"){
            player.discard()
            actionResult.actions.put(MapAction.DisallowMove,null)
            return actionResult
        }

        // Check actions
        if (ActionFlow.decideAction(player,ActionTrigger.ActionBtn,fightFactoryService, actionResult)){
            return actionResult
        }

        // Auto Map Transitions
        if (player.map.worldX != null && player.map.worldY != null){
            if (player.positionY == mapLayout.rows-1 && direction == "down"){ // down
                Map toMap = Map.findByWorldXAndWorldY(player.map.worldX,player.map.worldY+1)
                if (toMap){
                    MapLayout mapToLayout = MapLayout.createMapArray(toMap, AltMap.getAltMap(toMap, player))
                    String tile = mapToLayout.foreground[0][player.positionX]
                    if (tile == "" || tile == "0"){
                        player.positionY = 0
                        player.setMap toMap
                        player.save(flush:true)
                        actionResult.actions.put(MapAction.UpdateView, null)
                        return actionResult
                    }
                }
            }
            else if (player.positionY == 0 && direction == "up"){ // up
                Map toMap = Map.findByWorldXAndWorldY(player.map.worldX,player.map.worldY-1)
                if (toMap){
                    MapLayout mapToLayout = MapLayout.createMapArray(toMap, AltMap.getAltMap(toMap, player))
                    String tile = mapToLayout.foreground[mapToLayout.rows-1][player.positionX]
                    if (tile == "" || tile == "0"){
                        player.positionY = mapToLayout.rows-1
                        player.setMap toMap
                        player.save(flush:true)
                        actionResult.actions.put(MapAction.UpdateView, null)
                        return actionResult
                    }
                }
            }
            else if (player.positionX == 0 && direction == "left"){ // left
                Map toMap = Map.findByWorldXAndWorldY(player.map.worldX-1,player.map.worldY)
                if (toMap){
                    MapLayout mapToLayout = MapLayout.createMapArray(toMap, AltMap.getAltMap(toMap, player))
                    String tile = mapToLayout.foreground[player.positionY][mapToLayout.columns-1]
                    if (tile == "" || tile == "0"){
                        player.positionX = mapToLayout.columns-1
                        player.setMap toMap
                        player.save(flush:true)
                        actionResult.actions.put(MapAction.UpdateView, null)
                        return actionResult
                    }
                }
            }
            else if (player.positionX == mapLayout.columns-1 && direction == "right"){ // right
                Map toMap = Map.findByWorldXAndWorldY(player.map.worldX+1,player.map.worldY)
                if (toMap){
                    MapLayout mapToLayout = MapLayout.createMapArray(toMap, AltMap.getAltMap(toMap, player))
                    String tile = mapToLayout.foreground[player.positionY][0]
                    if (tile == "" || tile == "0"){
                        player.positionX = 0
                        player.setMap toMap
                        player.save(flush:true)
                        actionResult.actions.put(MapAction.UpdateView, null)
                        return actionResult
                    }
                }
            }
        }

        actionResult.actions.put(MapAction.DisallowMove, null)
        return actionResult
    }

    public ActionResult getMoveModel(Player player){
        MapLayout mapLayout = MapLayout.createMapArray(player.map, player.altMap)

        String currentBackgroundTile = getCurrentTile(mapLayout,player,true)
        ActionResult actionResult = new ActionResult()

        if (currentBackgroundTile){

            // check pokemon
            if (checkForWildPokemon(currentBackgroundTile)){
                MapPokemon mapPokemon = getRandomPokemon(player)
                if (mapPokemon){
                    Fight fight = fightFactoryService.startFight(BattleType.PVE,player,mapPokemon)

                    // koppel gevecht aan speler
                    player.fightNr = fight.nr
                    player.view = View.Battle
                    player.save(flush: true)

                    actionResult.actions.put(MapAction.UpdateViewAfterAnimation, null)
                    return actionResult
                }
            }

            // Check actions
            if (ActionFlow.decideAction(player,ActionTrigger.Move,fightFactoryService, actionResult)){
                if (!actionResult.actions[MapAction.DisallowMove]){
                    // The step is disallowed
                    player.discard()
                }
                else {
                    player.save(flush: true)
                }
                return actionResult
            }

            actionResult.actions.put(MapAction.AllowMove, null)
            return actionResult
        }

        String currentForegroundTile = getCurrentTile(mapLayout,player,false)
        if (!currentForegroundTile || currentForegroundTile != "0"){
            player.discard()
            actionResult.actions.put(MapAction.AllowMove, null)
            return actionResult
        }
    }

    /**
     * Tries to find an wild pokemon.
     * @param player
     */
    private MapPokemon getRandomPokemon(Player player)
    {
        Random random = new Random()

//        int encouterPkmnNr = random.nextInt(100)+1
//
//        if(encouterPkmnNr <= 10)
//        {
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
//        }
        return null
    }

    private boolean checkForWildPokemon(String currentTile){
        return currentTile == "01"
    }

    private String getCurrentTile(MapLayout mapLayout, Player player, boolean background)
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

}
