package game

import map.View

class BattleController {

    FightFactoryService fightFactoryService

    def exit(){
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        if (fight.battleOver){
            player.view = View.ShowMap
            player.save()
        }

        render text: g.render(template: 'getView')
    }

    def index() {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight

        if (player.fightNr != null){
            fight = fightFactoryService.getFight(player.fightNr)
        }

        if (player && fight){

            render view: 'index', model: [fight:fight]
        }
        else {
            render text:"No fight"
        }

    }

    def logRequest = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()
        //owner = owner.merge()
        Fight fight = fightFactoryService.getFight(player.fightNr)

        render text: g.render(template: 'log',model: [fight:fight])
    }

    def doMove = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        int ownerPokemonMoveId = Integer.parseInt(params.id)

        OwnerMove ownerMove = OwnerMove.findByIdAndOwnerPokemon(ownerPokemonMoveId,fight.fightPlayer1.ownerPokemon)
        Move move = ownerMove.move

        if (move == null || move.name == "Struggle") // Struggle || geen move
        {
            // Dit is geen eigen move, pp hoeft er niet af
            Moves.setMove(fight,fight.fightPlayer1, ownerMove.move)
        }
        else
        {

//            // Controlleer of de user klopt
//            if (ownerMove.ownerPokemon.owner != owner)
//            {
//                println "4"
//                render text:"Move bestaat niet voor pokemon"
//                return
//            }
//            else
//            {
                // :TODO implement
//                // controlleer pp
//                if (ownerPokemonMove->ppLeft != 0)
//                {
//                    if (isset(_SESSION["takePP"]) && _SESSION["takePP"] == false)
//                    {
//
//                        _SESSION["takePP"] = true;
//                    }
//                    else
//                    {

                if (ownerMove.ppLeft <= 0){
                    fight.log = "m:No pp left for move ${ownerMove.move.name}.;" // :TODO real message
                }
                else {
                    ownerMove.ppLeft -= 1
                    ownerMove.save()
                    Moves.setMove(fight,fight.fightPlayer1,ownerMove.move)
                }

//                }
//                else
//                {
//                    exit();
//                    //header("Location: index.php?action=fight");
//                }

                // After displaying last message we can destroy the fight
//                if (fight.battleOver){
//                    fightFactoryService.endFight(fight)
//                }

                render template: "log", model : [fight:fight]

//            }
        }
    }

    def menuRequest = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        // forget learn move
        if (fight.fightPlayer1.learnMoves && params.forgetMove != null)
        {
            removeLearnMove(fight)
        }
        // forget old move
        if (params.replaceMoveId != null){
            replaceMove(fight,Integer.parseInt(params.replaceMoveId))
        }
        // continue normal flow and show the menu


        // Replace old move
        if (fight.fightPlayer1.learnMoves && params.replaceMove != null)
        {
            render text: g.render(template: 'replaceMoveList', model: [ownerMoves:fight.fightPlayer1.ownerPokemon.ownerMoves])
        }
        // leer moves
        else if (fight.fightPlayer1.learnMoves)
        {
            // haal move op
            Move move = fight.fightPlayer1.learnMoves[0]

            render text: g.render(template: 'chooseLearnMove', model: [move:move,ownerPokemon: fight.fightPlayer1.ownerPokemon])
        }
        // moveList
        else if (fight.battleOver){
            render text: g.render(template: 'exit')
        }
        else if (fight.fightPlayer1.hp <= 0 && fight.fightPlayer2.hp > 0)
        {
            List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwnerAndPartyPositionGreaterThan(player,0)
            FightPlayer fightPlayer = fight.fightPlayer1
            render text: g.render(template: 'pokemonList',model: [mustChoose:true,ownerPokemonList:ownerPokemonList,fightPlayer:fightPlayer])
        }
        else if (params.items != null){
            render template: "itemList", model : [fight:fight,ownerItems:player.ownerItems]
        }
        else if (params.fight != null && !fight.battleOver)
        {
            Battle.beforeChosingMove(fight, myFightPlayer, player);

            render text: g.render(template: 'moveList', model: [ownerMoveList:myFightPlayer.ownerPokemon.ownerMoves])
        }
        // Switch pokemon list
        else if (params.pkmn != null && !fight.battleOver)
        {
            List<OwnerPokemon> ownerPokemonList = OwnerPokemon.findAllByOwnerAndPartyPositionGreaterThan(player,0)
            FightPlayer fightPlayer = fight.fightPlayer1
            render text: g.render(template: 'pokemonList',model: [mustChoose:false,ownerPokemonList:ownerPokemonList,fightPlayer:fightPlayer])
        }
        else {
            render text: g.render(template: 'actionList')
        }
    }

    def switchPokemon = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        OwnerPokemon ownerPokemon = OwnerPokemon.findByOwnerAndPartyPositionAndHpGreaterThan(player,params.id,0)
        if (ownerPokemon){
            fight.log = ""
            Stats.saveStats(fight.fightPlayer1, false);
            fight.fightPlayer1 = Stats.setBaseStats(fight,ownerPokemon, PlayerType.user, 1)
            Moves.setMove(fight,fight.fightPlayer1, null, false)
        }

        render template: "log", model : [fight:fight]
    }

    public static void removeLearnMove(Fight fight)
    {
        fight.fightPlayer1.learnMoves.remove(fight.fightPlayer1.learnMoves[0])
    }

    public static void replaceMove(Fight fight, int forgetMoveId)
    {
        OwnerMove oldOwnerMove = fight.fightPlayer1.ownerPokemon.ownerMoves.find { it.id == forgetMoveId }

        if (oldOwnerMove){
            Move learnMove = fight.fightPlayer1.learnMoves[0]
            if (learnMove){
                fight.fightPlayer1.ownerPokemon.removeFromOwnerMoves(oldOwnerMove)
                oldOwnerMove.delete()
                OwnerMove newOwnerMove = new OwnerMove(
                        ownerPokemon:fight.fightPlayer1.ownerPokemon,
                        move: learnMove,
                        ppLeft: learnMove.pp
                )

                fight.fightPlayer1.ownerPokemon.addToOwnerMoves(
                        newOwnerMove
                )
                fight.fightPlayer1.ownerPokemon.save()
                removeLearnMove(fight)
            }
        }
    }

    def run = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        Run.run(fight)

        render template: "log", model : [fight:fight]
    }

    def useItem = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)

        OwnerItem ownerItem = player.ownerItems.find() { it.id == Integer.parseInt(params.id) }

        if (ownerItem){
            UseItem.useItem(fight, fight.fightPlayer1.owner, ownerItem, fight.fightPlayer1, fight.fightPlayer2)
        }

        render template: "log", model : [fight:fight]
    }

}
