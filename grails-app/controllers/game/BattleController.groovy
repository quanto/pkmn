package game

import map.View

import game.fight.log.MessageLog
import game.fight.status.Stats
import game.context.FightPlayer
import game.context.PlayerType
import game.context.PlayerData
import game.context.Fight
import game.fight.Battle
import game.fight.UseItem
import game.fight.Run
import game.fight.action.MoveAction
import game.fight.action.SwitchAction
import game.fight.RoundResult
import game.context.BattleType
import game.fight.action.NoAction
import game.fight.ContinueMove
import game.context.FightPokemon

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
            FightPlayer myFightPlayer = fight.myPlayer(player)
            render view: 'index', model: [fight:fight, myFightPlayer:myFightPlayer]
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

        FightPlayer myFightPlayer = fight.myPlayer(player)

        render text: g.render(template: 'log',model: [fight:fight,myFightPlayer:myFightPlayer])
    }

    def doMove = {

        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        if (!fight.switchRound){

            int ownerPokemonMoveId = Integer.parseInt(params.id)

            OwnerMove ownerMove = OwnerMove.findByIdAndOwnerPokemon(ownerPokemonMoveId,myFightPlayer.fightPokemon.ownerPokemon)
            Move move = ownerMove.move

            if (move == null || move.name == "Struggle") // Struggle || geen move
            {
                // Dit is geen eigen move, pp hoeft er niet af
                Moves.setMove(fight,myFightPlayer, new MoveAction(move:ownerMove.move,ownerMoveForPP:null))
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
                        fight.roundResult.personalActions.add(new MessageLog("No pp left for move ${ownerMove.move.name}."))
                    }
                    else {
                        Moves.setMove(fight,myFightPlayer, new MoveAction(move:ownerMove.move,ownerMoveForPP:ownerMove))
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
            }

            render template: "log", model : [fight:fight,myFightPlayer:myFightPlayer]

//            }
        }
    }

    def menuRequest = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        // forget learn move
        if (myFightPlayer.learnMoves && params.forgetMove != null)
        {
            removeLearnMove(myFightPlayer)
        }
        // forget old move
        if (params.replaceMoveId != null){
            replaceMove(myFightPlayer,Integer.parseInt(params.replaceMoveId))
        }
        // continue normal flow and show the menu

        // We should set the switch action of the computer before setting our own action
        if (fight.switchRound && fight.battleType != BattleType.PVP){
            FightPokemon fightPokemon = myFightPlayer.opponentFightPlayer().party.find{ it.hp > 0 }
            myFightPlayer.opponentFightPlayer().battleAction = new SwitchAction(fightPokemon:fightPokemon)
        }



        // Replace old move
        if (myFightPlayer.learnMoves && params.replaceMove != null)
        {
            render text: g.render(template: 'replaceMoveList', model: [ownerMoves:myFightPlayer.fightPokemon.ownerPokemon.ownerMoves])
        }
        // leer moves
        else if (myFightPlayer.learnMoves)
        {
            // haal move op
            Move move = myFightPlayer.learnMoves[0]

            render text: g.render(template: 'chooseLearnMove', model: [move:move,fightPokemon: myFightPlayer.fightPokemon])
        }
        // moveList
        else if (fight.battleOver){
            render text: g.render(template: 'exit')
        }
        else if (myFightPlayer.waitOnOpponentMove){
            render text: g.render(template: 'waitOnOpponentMove')
        }
        else if (fight.switchRound && myFightPlayer.mustSwitch)
        {
            FightPlayer fightPlayer = myFightPlayer
            render text: g.render(template: 'pokemonList',model: [mustChoose:true,fightPokemonList:myFightPlayer.party,fightPlayer:fightPlayer])
        }
        // We should wait until the other switches
        else if ((fight.switchRound && !myFightPlayer.opponentFightPlayer().battleAction && params.pkmn == null))
        {
            render text: g.render(template: 'waitOnOpponentSwitch')
        }
        // Ask to switch
        else if (fight.switchRound && params.pkmn == null)
        {
            render text: g.render(template: 'chooseSwitchPokemon',model:[playerName:myFightPlayer.opponentFightPlayer().name,pokemonName:myFightPlayer.opponentFightPlayer().battleAction.fightPokemon.name])
        }
        // Switch pokemon list
        else if (params.pkmn != null && !fight.battleOver)
        {
            FightPlayer fightPlayer = myFightPlayer
            render text: g.render(template: 'pokemonList',model: [mustChoose:false,fightPokemonList:myFightPlayer.party,fightPlayer:fightPlayer])
        }
        else if (params.items != null){

            render template: "itemList", model : [fight:fight,ownerItems:player.ownerItems.findAll{ it.item in UsableItem }]
        }
        else if (params.fight != null && !fight.battleOver)
        {
            Battle.beforeChosingMove(fight, myFightPlayer, player);

            render text: g.render(template: 'moveList', model: [ownerMoveList:myFightPlayer.fightPokemon.ownerPokemon.ownerMoves])
        }
        else {
            if (ContinueMove.continueMove(fight,myFightPlayer)){
                render template: "refreshLog"
            }
            else {
                render text: g.render(template: 'actionList')
            }
        }
    }


    def noSwitch = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        if (fight.switchRound && myFightPlayer.fightPokemon.hp > 0){
            Moves.setMove(fight,myFightPlayer, new NoAction(), false)
        }

        render template: "log", model : [fight:fight,myFightPlayer:myFightPlayer]
    }

    def switchPokemon = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        int partyPosition = Integer.parseInt(params.id)
        FightPokemon fightPokemon = myFightPlayer.party.find() { it.hp > 0 && it.partyPosition == partyPosition}

        if (fightPokemon){
            Moves.setMove(fight,myFightPlayer, new SwitchAction( fightPokemon: fightPokemon ), false)
        }

        render template: "log", model : [fight:fight,myFightPlayer:myFightPlayer]
    }

    public static void removeLearnMove(FightPlayer fightPlayer)
    {
        fightPlayer.learnMoves.remove(fightPlayer.learnMoves[0])
    }

    public static void replaceMove(FightPlayer fightPlayer, int forgetMoveId)
    {
        OwnerMove oldOwnerMove = fightPlayer.fightPokemon.ownerPokemon.ownerMoves.find { it.id == forgetMoveId }

        if (oldOwnerMove){
            Move learnMove = fightPlayer.learnMoves[0]
            if (learnMove){
                fightPlayer.fightPokemon.ownerPokemon.removeFromOwnerMoves(oldOwnerMove)
                oldOwnerMove.delete()
                OwnerMove newOwnerMove = new OwnerMove(
                        ownerPokemon:fightPlayer.fightPokemon.ownerPokemon,
                        move: learnMove,
                        ppLeft: learnMove.pp
                )

                fightPlayer.fightPokemon.ownerPokemon.addToOwnerMoves(
                        newOwnerMove
                )
                fightPlayer.fightPokemon.ownerPokemon.save()
                removeLearnMove(fightPlayer)
            }
        }
    }

    def run = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()


        Fight fight = fightFactoryService.getFight(player.fightNr)
        if (!fight.switchRound){
            Run.run(fight)
        }
        FightPlayer myFightPlayer = fight.myPlayer(player)

        render template: "log", model : [fight:fight,myFightPlayer:myFightPlayer]
    }

    def useItem = {
        PlayerData playerData = session.playerData
        Player player = playerData.getPlayer()

        Fight fight = fightFactoryService.getFight(player.fightNr)
        FightPlayer myFightPlayer = fight.myPlayer(player)

        if (!fight.switchRound){

            OwnerItem ownerItem = player.ownerItems.find() { it.id == Integer.parseInt(params.id) }

            if (ownerItem){
                UseItem.setItemAction(fight, myFightPlayer, ownerItem)
            }

        }
        render template: "log", model : [fight:fight,myFightPlayer:myFightPlayer]
    }

}
